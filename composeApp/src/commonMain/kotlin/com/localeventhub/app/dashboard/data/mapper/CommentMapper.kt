package com.localeventhub.app.dashboard.data.mapper

import com.localeventhub.app.dashboard.data.model.CommentDto
import com.localeventhub.app.dashboard.domain.entity.CommentEntity

fun CommentEntity.toDto() = CommentDto(id,text, timestamp, user?.toDto())

fun CommentDto.toDomain() = CommentEntity(id,text, timestamp, userDto?.toDomain())