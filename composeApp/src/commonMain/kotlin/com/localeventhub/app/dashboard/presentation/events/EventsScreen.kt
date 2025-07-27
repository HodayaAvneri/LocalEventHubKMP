package com.localeventhub.app.dashboard.presentation.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.localeventhub.app.dashboard.domain.entity.PostEntity
import com.localeventhub.app.dashboard.domain.entity.UpdateResponse
import com.localeventhub.app.dashboard.domain.usecase.DeletePostUseCase
import com.localeventhub.app.dashboard.presentation.navigation.EventPageFlag
import com.localeventhub.app.featurebase.common.Colors
import com.localeventhub.app.featurebase.common.PreferenceStorage
import com.localeventhub.app.featurebase.common.PrefsDataStore
import com.localeventhub.app.featurebase.common.Status
import com.localeventhub.app.featurebase.common.UIStatus
import com.localeventhub.app.featurebase.common.Utils
import com.localeventhub.app.featurebase.presentation.ui.compose.LoadingProgress
import com.localeventhub.app.featurebase.presentation.ui.compose.LocationTracking
import com.localeventhub.app.featurebase.presentation.ui.compose.TextTitleMedium
import com.localeventhub.app.featurebase.presentation.ui.state.UIState
import dev.jordond.compass.Location
import localeventhub.composeapp.generated.resources.Res
import localeventhub.composeapp.generated.resources.events
import localeventhub.composeapp.generated.resources.ic_add
import localeventhub.composeapp.generated.resources.ic_menu
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
    paddingValues: () -> PaddingValues,
    onNavigate: (EventPageFlag, String) -> Unit,
    viewModel: EventViewModel = koinViewModel()
) {

    val getAllPostResponse by viewModel.getEventListResponse.collectAsState(initial = null)
    val deletePostResponse by viewModel.deleteEventResponse.collectAsState(initial = null)
    var uiState by remember { mutableStateOf<UIState<List<PostEntity>?>>(UIState.initial()) }
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("All", "Music Festival", "Food Festival", "Donors event")
    var selectedItem by remember { mutableStateOf(items[0]) }
    var sortExpanded by remember { mutableStateOf(false) }
    val sortItems = listOf("Select", "Distance")
    var sortSelectedItem by remember { mutableStateOf(sortItems[0]) }
    var getCurrentLocation by remember { mutableStateOf(false) }
    val platform = remember {
        com.localeventhub.app.getPlatform().name
    }
    val utils = remember { Utils() }
    val postList = remember {
        mutableStateListOf<PostEntity>()
    }
    val filteredPostList = remember {
        mutableStateListOf<PostEntity>()
    }
    LaunchedEffect(Unit) {
        viewModel.getAllPost()
    }

    LaunchedEffect(getAllPostResponse?.status) {
        when (getAllPostResponse?.status) {
            Status.SUCCESS -> {
                val response = getAllPostResponse?.data
                response?.let {
                    postList.clear()
                    filteredPostList.clear()
                    postList.addAll(response)
                    filteredPostList.addAll(response)
                }
                uiState = UIState.success(response)
            }

            Status.ERROR -> {
                uiState = UIState.error(getAllPostResponse?.message ?: "")

            }

            Status.LOADING -> {
                uiState = UIState.loading()
            }

            null -> {}
        }
    }

    LaunchedEffect(deletePostResponse?.status) {
        when (deletePostResponse?.status) {
            Status.SUCCESS -> {
                viewModel.clearState()
                viewModel.getAllPost()
            }

            Status.LOADING -> {
                uiState = UIState.loading()
            }

            else -> {
                viewModel.clearState()
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
            .padding(bottom = paddingValues().calculateBottomPadding()),
        topBar = {
            TopAppBar(
                modifier = Modifier.shadow(elevation = 5.dp),
                title = {
                    Text(
                        stringResource(Res.string.events),
                        color = if (platform.startsWith("Android")) Color.White else Color.Black
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (platform.startsWith(
                            "Android"
                        )
                    ) Colors.primary else Color.White
                ),
                actions = {
                    if (!platform.startsWith(prefix = "Android")) {
                        IconButton(onClick = { onNavigate(EventPageFlag.ADD, "") }) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_add),
                                contentDescription = "Add",
                                tint = Color.Black
                            )
                        }
                    }
                }

            )
        },
        containerColor = Color.White,
        floatingActionButton = {
            if (platform.startsWith("Android")) {
                FloatingActionButton(onClick = { onNavigate(EventPageFlag.ADD, "") }) {
                    Icon(painter = painterResource(Res.drawable.ic_add), contentDescription = null)
                }
            }
        }
    ) { innerPadding ->
        when (uiState.status) {
            UIStatus.SUCCESS -> {
                    Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                        Row(modifier = Modifier.fillMaxWidth().padding(top = 5.dp)) {
                            ExposedDropdownMenuBox(
                                modifier = Modifier.fillMaxWidth(0.5f).padding(start = 15.dp),
                                expanded = expanded,
                                onExpandedChange = { expanded = !expanded }
                            ) {
                                // TextField with dropdown icon
                                OutlinedTextField(
                                    value = selectedItem,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Filter") },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = expanded
                                        )
                                    },
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth()
                                )

                                // Dropdown menu items
                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    items.forEach { item ->
                                        DropdownMenuItem(
                                            text = { Text(item) },
                                            onClick = {
                                                selectedItem = item
                                                expanded = false
                                                filteredPostList.clear()
                                                filteredPostList.addAll(if(selectedItem == "All") postList else postList.filter { it.tags.contains(selectedItem) })
                                            }
                                        )
                                    }
                                }
                            }
                            ExposedDropdownMenuBox(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(start = 15.dp, end = 15.dp),
                                expanded = sortExpanded,
                                onExpandedChange = { sortExpanded = !sortExpanded }
                            ) {
                                // TextField with dropdown icon
                                OutlinedTextField(
                                    value = sortSelectedItem,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Sort") },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = expanded
                                        )
                                    },
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth()
                                )

                                // Dropdown menu items
                                ExposedDropdownMenu(
                                    expanded = sortExpanded,
                                    onDismissRequest = { sortExpanded = false }
                                ) {
                                    sortItems.forEach { item ->
                                        DropdownMenuItem(
                                            text = { Text(item) },
                                            onClick = {
                                                sortSelectedItem = item
                                                sortExpanded = false
                                                if(sortSelectedItem == "Distance"){
                                                    getCurrentLocation = true
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        if(getCurrentLocation){
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                                CircularProgressIndicator()
                            }
                            LocationTracking { location: Location ->
                                println(location)
                                filteredPostList.clear()
                                filteredPostList.addAll(postList
                                    .filter { it.location != null }
                                    .sortedBy { post ->
                                        val loc = post.location!!
                                        utils.haversineDistance(location.coordinates.latitude, location.coordinates.longitude, loc.latitude, loc.longitude)
                                    })
                                getCurrentLocation = false
                            }
                        }else{
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                items(filteredPostList) { post ->
                                    PostItem(post, onNavigate, onDelete = { id ->
                                        viewModel.deletePost(id)
                                    }, onLike = { id, list ->
                                        viewModel.updateLike(id, list)
                                    })
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
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

            UIStatus.ERROR -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextTitleMedium(text = "Try Again!!!")
                }
            }

        }

    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostItem(
    post: PostEntity,
    onNavigate: (EventPageFlag, String) -> Unit,
    onDelete: (String) -> Unit,
    onLike: (String, List<String>) -> Unit
) {
    println(post)
    var menuExpand by remember { mutableStateOf(false) }
    val dataStore = koinInject<PrefsDataStore>()
    var isLiked by remember { mutableStateOf(false) }
    val utils = remember { Utils() }
    var userid = ""
    var openCommentSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        userid = PreferenceStorage.getData(dataStore, "user_id").toString()
        isLiked = post.likedBy.contains(userid)
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp)
        ) {
            // User info
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    model = post.user?.profileImageUrl ?: "",
                    contentDescription = ""
                ) // Light green for avatar placeholder
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = post.user?.name ?: "", fontWeight = FontWeight.Bold)
                    if (post.timestamp > 0)
                        Text(
                            text = utils.daysUntilTargetDate(post.timestamp),
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                }
                Spacer(modifier = Modifier.weight(1f))
                Box {
                    IconButton(onClick = { menuExpand = true }) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_menu),
                            contentDescription = "More"
                        )
                    }

                    DropdownMenu(
                        expanded = menuExpand,
                        onDismissRequest = { menuExpand = false }
                    ) {
                        DropdownMenuItem(text = { Text(text = "View") }, onClick = {
                            menuExpand = false
                            onNavigate(EventPageFlag.VIEW, post.postId)
                        })
                        if(userid == post.userId){
                            DropdownMenuItem(text = { Text(text = "Edit") }, onClick = {
                                menuExpand = false
                                onNavigate(EventPageFlag.UPDATE, post.postId)
                            })
                            DropdownMenuItem(text = { Text(text = "Delete") }, onClick = {
                                menuExpand = false
                                onDelete(post.postId)
                            })
                        }
                    }
                }

            }

            Spacer(modifier = Modifier.height(8.dp))

            // Image placeholder
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp)),
                model = post.imageUrl,
                contentDescription = "",
                contentScale = ContentScale.Crop
            ) // Light green placeholder for image

            Spacer(modifier = Modifier.height(8.dp))
            // Post content
            Text(text = post.description)
            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider(color = Color.LightGray)
            // Action buttons
            Row {
                Button(
                    onClick = {
                        isLiked = !isLiked
                        if (isLiked) {
                            post.likedBy.add(userid)
                        } else {
                            post.likedBy.remove(userid)
                        }
                        onLike(post.postId, post.likedBy)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        if (isLiked) "Liked" else "Like",
                        color = if (isLiked) Colors.primary else Color.Black
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { openCommentSheet = true},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Comment", color = Color.Black)
                }
            }
        }
    }
    if (openCommentSheet)
        CommentsBottomSheet(
            postId = { post.postId } ,
            onDismiss = {openCommentSheet = false}
        )
}





