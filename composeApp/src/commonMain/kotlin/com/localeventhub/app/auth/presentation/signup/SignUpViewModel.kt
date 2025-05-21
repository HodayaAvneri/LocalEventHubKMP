package com.localeventhub.app.auth.presentation.signup

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
import com.localeventhub.app.featurebase.common.ApiResult
import com.localeventhub.app.featurebase.presentation.ui.state.TextFieldState

class SignUpViewModel(private val authUseCase: AuthUseCase): ViewModel() {

    private val _nameState = mutableStateOf(TextFieldState())
    var nameState: State<TextFieldState> = _nameState

    private val _emailState = mutableStateOf(TextFieldState())
    var emailState: State<TextFieldState> = _emailState

    private val _passwordState = mutableStateOf(TextFieldState())
    var passwordState: State<TextFieldState> = _passwordState

    private val _confirmPasswordState = mutableStateOf(TextFieldState())
    var confirmPasswordState: State<TextFieldState> = _confirmPasswordState

    private val _signUpResponse = MutableStateFlow<ApiResult<AuthResponse>?>(null)
    var signUpResponse = _signUpResponse.asStateFlow()

    fun setName(name: String) {
        _nameState.value = _nameState.value.copy(textValue = name, isError = name.isEmpty())
    }

    fun setEmail(email: String) {
        _emailState.value = _emailState.value.copy(textValue = email, isError = email.isEmpty())
    }

    fun setPassword(password: String) {
        _passwordState.value = _passwordState.value.copy(textValue = password, isError = password.isEmpty())
    }

    fun setConfirmPassword(confirmPassword: String) {
        _confirmPasswordState.value = _confirmPasswordState.value.copy(textValue = confirmPassword, isError = confirmPassword.isEmpty())
    }

    fun login(authRequest: AuthRequest){

        viewModelScope.launch {
            authUseCase.invoke(authRequest).collect{
                _signUpResponse.value = it
            }
        }
    }

}