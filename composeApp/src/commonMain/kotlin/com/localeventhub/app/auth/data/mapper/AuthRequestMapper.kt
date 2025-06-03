package com.localeventhub.app.auth.data.mapper

import com.localeventhub.app.auth.data.model.AuthRequestDto
import com.localeventhub.app.auth.domain.entity.AuthRequest

fun AuthRequest.toDto() = AuthRequestDto(this.email,this.password)