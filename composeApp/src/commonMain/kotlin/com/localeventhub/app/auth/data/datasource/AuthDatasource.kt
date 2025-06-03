package com.localeventhub.app.auth.data.datasource

import com.localeventhub.app.auth.data.model.AuthRequestDto
import com.localeventhub.app.auth.data.model.UserDto

class AuthDatasource(private val apiService: AuthFirebaseService) {
    suspend fun signUp(authRequestDto: AuthRequestDto, userDto: UserDto) = apiService.signUp(authRequestDto, userDto)
    suspend fun signIn(authRequestDto: AuthRequestDto) = apiService.signIn(authRequestDto)
}