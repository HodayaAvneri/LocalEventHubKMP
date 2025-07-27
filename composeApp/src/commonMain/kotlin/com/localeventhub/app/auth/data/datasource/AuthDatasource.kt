package com.localeventhub.app.auth.data.datasource

import com.localeventhub.app.auth.data.model.AuthRequestDto
import com.localeventhub.app.auth.data.model.AuthResponseDto
import com.localeventhub.app.auth.data.model.UserDto

interface AuthDatasource{
    suspend fun signUp(authRequestDto: AuthRequestDto, userDto: UserDto): AuthResponseDto
    suspend fun signIn(authRequestDto: AuthRequestDto): AuthResponseDto
}