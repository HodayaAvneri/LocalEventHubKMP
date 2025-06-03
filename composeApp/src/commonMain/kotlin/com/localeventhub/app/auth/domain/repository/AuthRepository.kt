package com.localeventhub.app.auth.domain.repository

import com.localeventhub.app.auth.domain.entity.AuthRequest
import com.localeventhub.app.auth.domain.entity.AuthResponse
import com.localeventhub.app.auth.domain.entity.UserEntity

interface AuthRepository {

    suspend fun signUp(authRequest: AuthRequest, userRequest: UserEntity): AuthResponse
    suspend fun signIn(authRequest: AuthRequest): AuthResponse
}