package com.localeventhub.app.dashboard.presentation.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.localeventhub.app.dashboard.domain.entity.UpdateResponse
import com.localeventhub.app.dashboard.domain.entity.UserEntity
import com.localeventhub.app.dashboard.domain.usecase.UpdateUserUseCase
import com.localeventhub.app.featurebase.common.ApiResult
import com.localeventhub.app.featurebase.common.Validation
import com.localeventhub.app.featurebase.presentation.ui.state.TextFieldState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val updateUserUseCase: UpdateUserUseCase) : ViewModel() {

    var imagePath by mutableStateOf("")
    lateinit var userId: String
    var isProfileImageUpdated = false
    private val _nameState = mutableStateOf(TextFieldState())
    var nameState: State<TextFieldState> = _nameState

    private val _emailState = mutableStateOf(TextFieldState())
    var emailState: State<TextFieldState> = _emailState

    private val _updateProfileResponse = MutableStateFlow<ApiResult<UpdateResponse>?>(null)
    var updateProfileResponse = _updateProfileResponse.asStateFlow()

    fun setInitData(userId: String, imagePath: String, name: String, email: String){
        this.userId = userId
        this.imagePath = imagePath
        _nameState.value = TextFieldState(textValue = name)
        _emailState.value = TextFieldState(textValue = email)
    }

    fun setName(name: String) {
        _nameState.value = _nameState.value.copy(textValue = name, isError = name.isEmpty())
    }

    fun setEmail(email: String) {
        _emailState.value = _emailState.value.copy(textValue = email, isError = email.isEmpty())
    }

    fun validateAndUpdate() {

        if (_nameState.value.textValue.isEmpty()) {
            _nameState.value = _nameState.value.copy(isError = true)
            return
        }
        /*if (_emailState.value.textValue.isEmpty()) {
            _emailState.value = _emailState.value.copy(isError = true)
            return
        }
        if (!Validation.isEmailValid(_emailState.value.textValue)) {
            _emailState.value = _emailState.value.copy(isError = true)
            return
        }*/
        updateProfile()
    }

    private fun updateProfile() {
        viewModelScope.launch {
            updateUserUseCase.invoke(
                UserEntity(userId, nameState.value.textValue, emailState.value.textValue,imagePath, isProfileImageUpdated)
            ).collect {
                _updateProfileResponse.value = it
            }
        }
    }

    fun clearAuthState(){
        _updateProfileResponse.value = null
    }

}