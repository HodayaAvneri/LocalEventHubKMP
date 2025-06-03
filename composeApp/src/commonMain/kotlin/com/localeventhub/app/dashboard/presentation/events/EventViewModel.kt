package com.localeventhub.app.dashboard.presentation.events

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.localeventhub.app.auth.domain.entity.AuthRequest
import com.localeventhub.app.auth.domain.entity.AuthResponse
import com.localeventhub.app.auth.domain.usecase.AuthUseCase
import com.localeventhub.app.dashboard.presentation.navigation.EventPageFlag
import com.localeventhub.app.featurebase.common.ApiResult
import com.localeventhub.app.featurebase.presentation.ui.state.TextFieldState
import io.ktor.sse.END_OF_LINE

class EventViewModel(): ViewModel() {

    //val pageFlag: String = savedStateHandle.get<String>("pageFlag") ?: ""

    val pageFlag = ""

    private val _descriptionState = mutableStateOf(TextFieldState())
    var descriptionState: State<TextFieldState> = _descriptionState

    private val _locationState = mutableStateOf(TextFieldState())
    var locationState: State<TextFieldState> = _locationState

    private val _createEventResponse = MutableStateFlow<ApiResult<AuthResponse>?>(null)
    var eventListResponse = _createEventResponse.asStateFlow()

    fun setDescription(description: String) {
        _descriptionState.value = _descriptionState.value.copy(textValue = description, isError = description.isEmpty())
    }

    fun setLocation(location: String) {
        _locationState.value = _locationState.value.copy(textValue = location, isError = location.isEmpty())
    }


}