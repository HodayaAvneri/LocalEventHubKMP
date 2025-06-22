package com.localeventhub.app.dashboard.data.mapper

import com.localeventhub.app.dashboard.data.model.UserDto
import com.localeventhub.app.dashboard.domain.entity.UserEntity


fun UserEntity.toDto() = UserDto(this.userId, this.name, this.email, this.profileImageUrl, this.shouldUpdateProfile,this.updatedAt)

fun UserDto.toDomain() = UserEntity(this.userId, this.name, this.email, this.profileImageUrl, this.shouldUpdateProfile,this.updatedAt)