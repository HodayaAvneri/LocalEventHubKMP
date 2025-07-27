package com.localeventhub.app.dashboard.data.mapper

import com.localeventhub.app.dashboard.data.model.AddCommentResponseDto
import com.localeventhub.app.dashboard.domain.entity.AddCommentResponseEntity

fun AddCommentResponseEntity.toDto() = AddCommentResponseDto(commentEntity?.toDto(), message)
fun AddCommentResponseDto.toDomain() = AddCommentResponseEntity(commentEntity?.toDomain(), message)