package com.localeventhub.app.dashboard.presentation.events

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.localeventhub.app.dashboard.domain.entity.AddCommentResponseEntity
import com.localeventhub.app.dashboard.domain.entity.CommentEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.localeventhub.app.dashboard.domain.entity.EventLocationEntity
import com.localeventhub.app.dashboard.domain.entity.PostEntity
import com.localeventhub.app.dashboard.domain.entity.UpdateResponse
import com.localeventhub.app.dashboard.domain.entity.UserEntity
import com.localeventhub.app.dashboard.domain.usecase.AddCommentUseCase
import com.localeventhub.app.dashboard.domain.usecase.AddPostUseCase
import com.localeventhub.app.dashboard.domain.usecase.DeleteCommentUseCase
import com.localeventhub.app.dashboard.domain.usecase.DeletePostUseCase
import com.localeventhub.app.dashboard.domain.usecase.GetAllPostUseCase
import com.localeventhub.app.dashboard.domain.usecase.GetCommentsUseCase
import com.localeventhub.app.dashboard.domain.usecase.GetPostUseCase
import com.localeventhub.app.dashboard.domain.usecase.UpdatePostLikeUseCase
import com.localeventhub.app.dashboard.domain.usecase.UpdatePostUseCase
import com.localeventhub.app.dashboard.presentation.navigation.EventPageFlag
import com.localeventhub.app.featurebase.common.ApiResult
import com.localeventhub.app.featurebase.presentation.ui.state.TextFieldState
import kotlinx.coroutines.launch

class EventViewModel(
    savedStateHandle: SavedStateHandle,
    private val addPostUseCase: AddPostUseCase,
    private val getAllPostUseCase: GetAllPostUseCase,
    private val getPostUseCase: GetPostUseCase,
    private val updatePostUseCase: UpdatePostUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val updateListUseCase: UpdatePostLikeUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val getAllCommentsUseCase: GetCommentsUseCase,
) : ViewModel() {
    val pageFlag: String = savedStateHandle.get<String>("pageFlag") ?: ""
    var postId: String = savedStateHandle.get<String>("id") ?: ""
    var post: PostEntity?= null
    var postImage = mutableStateOf("")
    var isPostImageUpdated = false
    var tag = ""
    var location: EventLocationEntity? = null

    private val _descriptionState = mutableStateOf(TextFieldState())
    var descriptionState: State<TextFieldState> = _descriptionState

    private val _createEventResponse = MutableStateFlow<ApiResult<UpdateResponse>?>(null)
    var createEventResponse = _createEventResponse.asStateFlow()

    private val _getEventListResponse = MutableStateFlow<ApiResult<List<PostEntity>>?>(null)
    var getEventListResponse = _getEventListResponse.asStateFlow()

    private val _getEventResponse = MutableStateFlow<ApiResult<PostEntity?>?>(null)
    var getEventResponse = _getEventResponse.asStateFlow()

    private val _deleteEventResponse = MutableStateFlow<ApiResult<UpdateResponse>?>(null)
    var deleteEventResponse = _deleteEventResponse.asStateFlow()

    private val _commentState = mutableStateOf(TextFieldState())
    var commentState: State<TextFieldState> = _commentState

    private val _getCommentsResponse = MutableStateFlow<ApiResult<List<CommentEntity>>?>(null)
    var getCommentResponse = _getCommentsResponse.asStateFlow()

    private val _addCommentResponse = MutableStateFlow<ApiResult<AddCommentResponseEntity>?>(null)
    var addCommentResponse = _addCommentResponse.asStateFlow()

    init {
        if (postId.isNotEmpty()) {
            getPost()
        }
    }

    fun setDescription(description: String) {
        _descriptionState.value =
            _descriptionState.value.copy(textValue = description, isError = description.isEmpty())
    }

    fun setComment(comment: String) {
        _commentState.value =
            _commentState.value.copy(textValue = comment, isError = comment.isEmpty())
    }

    fun validateAndAddPost() {
        if (_descriptionState.value.textValue.isEmpty()) {
            _descriptionState.value = _descriptionState.value.copy(isError = true)
            return
        }
        if(pageFlag == EventPageFlag.ADD.name)
           addPost()
        else
            updatePost()
    }

    private fun addPost() {
        viewModelScope.launch {
            addPostUseCase.invoke(
                PostEntity(
                    "",
                    "",
                    descriptionState.value.textValue,
                    postImage.value,
                    location,
                    tags = listOf(tag),
                    user = null
                )
            ).collect {
                _createEventResponse.value = it
            }
        }
    }

    fun getAllPost() {
        viewModelScope.launch {
            getAllPostUseCase.invoke().collect {
                _getEventListResponse.value = it
            }
        }
    }

    fun clearState() {
        _createEventResponse.value = null
        _getEventResponse.value = null
        _deleteEventResponse.value = null
    }

    private fun getPost() {
        viewModelScope.launch {
            getPostUseCase.invoke(postId).collect {
                _getEventResponse.value = it
            }
        }
    }

    private fun updatePost() {
        viewModelScope.launch {
            post?.let { it ->
                updatePostUseCase.invoke(
                    PostEntity(
                        postId,
                        it.userId,
                        descriptionState.value.textValue,
                        postImage.value,
                        location,
                        tags = listOf(tag),
                        user = it.user,
                        shouldUpdatePhoto = isPostImageUpdated
                    )
                ).collect {
                    _createEventResponse.value = it
                }
            }

        }
    }

    fun deletePost(id: String,) {
        viewModelScope.launch {
            deletePostUseCase.invoke(id).collect {
                _deleteEventResponse.value = it
            }
        }
    }

    fun updateLike(id: String,likeCount: List<String>) {
        viewModelScope.launch {
            updateListUseCase.invoke(id,likeCount).collect {
                _createEventResponse.value = it
            }
        }
    }

    fun validateAndAddComment(postId: String) {
        if (_commentState.value.textValue.isEmpty()) {
            _commentState.value = _commentState.value.copy(isError = true)
            return
        }
        addComment(postId)
    }

    fun getComments(postId: String) {
        viewModelScope.launch {
            getAllCommentsUseCase.invoke(postId).collect{
                _getCommentsResponse.value = it
            }
        }
    }

    private fun addComment(postId: String) {
        viewModelScope.launch {
            addCommentUseCase.invoke(postId, CommentEntity("",_commentState.value.textValue, user = null)).collect{
                _addCommentResponse.value = it
            }
        }
    }

    fun deleteComment(postId: String, commentId: String) {
        viewModelScope.launch {
            deleteCommentUseCase.invoke(postId, commentId).collect{
                //_addCommentResponse.value = it
            }
        }
    }

}