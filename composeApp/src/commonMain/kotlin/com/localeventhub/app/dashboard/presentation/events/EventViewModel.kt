package com.localeventhub.app.dashboard.presentation.events

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.localeventhub.app.dashboard.domain.entity.EventLocation
import com.localeventhub.app.dashboard.domain.entity.PostEntity
import com.localeventhub.app.dashboard.domain.entity.UpdateResponse
import com.localeventhub.app.dashboard.domain.usecase.AddPostUseCase
import com.localeventhub.app.dashboard.domain.usecase.GetAllPostUseCase
import com.localeventhub.app.dashboard.domain.usecase.GetPostUseCase
import com.localeventhub.app.featurebase.common.ApiResult
import com.localeventhub.app.featurebase.common.Status
import com.localeventhub.app.featurebase.presentation.ui.state.TextFieldState
import kotlinx.coroutines.launch

class EventViewModel(
    savedStateHandle: SavedStateHandle,
    private val addPostUseCase: AddPostUseCase,
    private val getAllPostUseCase: GetAllPostUseCase,
    private val getPostUseCase: GetPostUseCase
) : ViewModel() {

    val pageFlag: String = savedStateHandle.get<String>("pageFlag") ?: ""
    val postId: String = savedStateHandle.get<String>("id") ?: ""

    var postImage = mutableStateOf("")
    var tag = ""
    var location: EventLocation? = null
    private val _descriptionState = mutableStateOf(TextFieldState())
    var descriptionState: State<TextFieldState> = _descriptionState

    private val _createEventResponse = MutableStateFlow<ApiResult<UpdateResponse>?>(null)
    var createEventResponse = _createEventResponse.asStateFlow()

    private val _eventListResponse = MutableStateFlow<ApiResult<List<PostEntity>>?>(null)
    var eventListResponse = _eventListResponse.asStateFlow()

    private val _eventResponse = MutableStateFlow<ApiResult<PostEntity?>?>(null)
    var eventResponse = _eventResponse.asStateFlow()


    init {
        if (postId.isNotEmpty()) {
            getPost()
        }
    }

    fun setDescription(description: String) {
        _descriptionState.value =
            _descriptionState.value.copy(textValue = description, isError = description.isEmpty())
    }

    fun validateAndAddPost() {

        if (_descriptionState.value.textValue.isEmpty()) {
            _descriptionState.value = _descriptionState.value.copy(isError = true)
            return
        }
        addPost()
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
                _eventListResponse.value = it
            }
        }
    }

    fun clearAddPostState() {
        _createEventResponse.value = null
        _eventResponse.value = null
    }

    fun getPost() {
        viewModelScope.launch {
            getPostUseCase.invoke(postId).collect {
                _eventResponse.value = it
            }
        }
    }

}