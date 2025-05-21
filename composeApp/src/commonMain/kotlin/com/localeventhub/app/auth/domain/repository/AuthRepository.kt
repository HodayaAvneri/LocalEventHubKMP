package com.localeventhub.app.auth.domain.repository

import com.localeventhub.app.auth.domain.entity.AuthRequest
import com.localeventhub.app.auth.domain.entity.AuthResponse

interface AuthRepository {
    suspend fun signIn(authRequest: AuthRequest): AuthResponse
}