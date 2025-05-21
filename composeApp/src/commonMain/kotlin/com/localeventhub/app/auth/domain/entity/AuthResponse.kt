package com.localeventhub.app.auth.domain.entity

data class AuthResponse(val accessToken: String?, val refreshToken: String?, val message: String?)
