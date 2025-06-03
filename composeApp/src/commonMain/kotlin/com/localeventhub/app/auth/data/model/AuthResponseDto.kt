package com.localeventhub.app.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseDto(val uID: String?, val message: String?)
