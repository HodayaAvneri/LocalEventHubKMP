package com.localeventhub.app.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseDto(val accessToken: String?, val refreshToken: String?, val message: String?)
