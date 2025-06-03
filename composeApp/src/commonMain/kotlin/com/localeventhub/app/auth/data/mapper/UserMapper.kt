package com.localeventhub.app.auth.data.mapper

import com.localeventhub.app.auth.data.model.UserDto
import com.localeventhub.app.auth.domain.entity.UserEntity

fun UserEntity.toDto() = UserDto(this.userId, this.name, this.email, this.profileImageUrl, this.createdAt, this.updatedAt)