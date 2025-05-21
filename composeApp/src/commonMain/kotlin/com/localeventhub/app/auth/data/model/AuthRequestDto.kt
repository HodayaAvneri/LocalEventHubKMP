package com.localeventhub.app.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequestDto(val email: String, val password: String)
