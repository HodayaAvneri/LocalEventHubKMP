package com.localeventhub.app.dashboard.presentation.events

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.localeventhub.app.dashboard.domain.entity.AddCommentResponseEntity
import com.localeventhub.app.dashboard.domain.entity.CommentEntity
import com.localeventhub.app.dashboard.domain.entity.UpdateResponse
import com.localeventhub.app.featurebase.common.Status
import com.localeventhub.app.featurebase.common.UIStatus
import com.localeventhub.app.featurebase.presentation.ui.compose.LoadingProgress
import com.localeventhub.app.featurebase.presentation.ui.state.UIState
import kotlinx.coroutines.delay
import multiplatform.network.cmptoast.showToast
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.component.getScopeName


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsBottomSheet(
    postId: () -> String,
    modifier: Modifier = Modifier.padding(top = 56.dp),
    onDismiss: () -> Unit,
    viewModel: EventViewModel = koinViewModel()
) {
    // State for bottom sheet
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val allCommentsResponse by viewModel.getCommentResponse.collectAsState(initial = null)
    var uiState by remember { mutableStateOf<UIState<List<CommentEntity>?>>(UIState.initial()) }
    var addCommentUIState by remember { mutableStateOf<UIState<AddCommentResponseEntity?>>(UIState.initial()) }
    val commentList = remember {
        mutableStateListOf<CommentEntity>()
    }
    val addCommentsResponse by viewModel.addCommentResponse.collectAsState(initial = null)

    LaunchedEffect(bottomSheetState.currentValue){
        if (bottomSheetState.currentValue == SheetValue.Expanded) {
            commentList.clear()
            viewModel.getComments(postId())
        }
    }

    // Launched effect to show the bottom sheet on composition
    LaunchedEffect(Unit) {
        bottomSheetState.expand()
    }

    LaunchedEffect(allCommentsResponse?.status) {
        when (allCommentsResponse?.status) {
            Status.SUCCESS -> {
                val response = allCommentsResponse?.data
                response?.let {
                    commentList.clear()
                    commentList.addAll(response)
                }
                uiState = UIState.success(response)
            }

            Status.ERROR -> {
                uiState = UIState.error(allCommentsResponse?.message ?: "")
            }

            Status.LOADING -> {
                uiState = UIState.loading()
            }

            null -> {}
        }
    }

    LaunchedEffect(addCommentsResponse?.status){
        when (addCommentsResponse?.status) {
            Status.SUCCESS -> {
                val response = addCommentsResponse?.data
                addCommentUIState = UIState.success(response)
                response?.let {
                     commentList.add(it.commentEntity!!)
                }
            }
            Status.LOADING -> {
                addCommentUIState = UIState.loading()
            }
            Status.ERROR -> {
                addCommentUIState = UIState.error("")
                showToast("Error occurred")
            }
            else -> {}
        }
    }

    ModalBottomSheet(
        modifier = modifier,
        sheetState = bottomSheetState,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        onDismissRequest = onDismiss,
        content = {
            when (uiState.status) {
                UIStatus.SUCCESS -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF5F5F5))
                            .padding(16.dp)
                    ) {
                        // Header
                        Text(
                            text = "comments",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // LazyColumn for comments
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            items(commentList) { comment ->
                                CommentItem(
                                    comment = comment,
                                    onDelete = {
                                       viewModel.deleteComment(postId(),comment.id)
                                       commentList.remove(comment)
                                    }
                                )
                            }
                        }
                        if(addCommentUIState.status == UIStatus.LOADING){
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        }
                        // Comment input area
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = viewModel.commentState.value.textValue,
                                onValueChange = viewModel::setComment,
                                placeholder = { Text("Write a comment") },
                                modifier = Modifier
                                    .weight(1f)
                                    .background(Color.White, RoundedCornerShape(8.dp)),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedTextColor = Color.Black,
                                )
                            )
                            Button(
                                onClick = { viewModel.validateAndAddComment(postId = postId()) },
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFFD81B60
                                    )
                                )
                            ) {
                                Text("SEND", color = Color.White)
                            }
                        }
                    }
                }

                UIStatus.INITIAL, UIStatus.LOADING -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LoadingProgress()
                    }
                }

                else -> {
                }
            }
        }
    )
}

@Composable
fun CommentItem(
    comment: CommentEntity,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    println(comment)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(8.dp)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // User avatar placeholder
        AsyncImage(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            model = comment.user?.profileImageUrl ?: "",
            contentDescription = ""
        )

        // Comment details
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = comment.user?.name ?: "",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = comment.text,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        // Delete icon
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete comment",
            tint = Color(0xFFD81B60),
            modifier = Modifier
                .size(24.dp)
                .clickable { onDelete() }
        )
    }
}
