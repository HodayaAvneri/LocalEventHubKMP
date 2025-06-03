package com.localeventhub.app.auth.data.mapper

import com.localeventhub.app.auth.data.model.AuthResponseDto
import com.localeventhub.app.auth.domain.entity.AuthResponse

fun AuthResponseDto.toDomain() = AuthResponse(this.uID, this.message)