package com.localeventhub.app.dashboard.data.mapper

import com.localeventhub.app.dashboard.data.model.UpdateResponseDto
import com.localeventhub.app.dashboard.domain.entity.UpdateResponse

fun UpdateResponseDto.toDomain() = UpdateResponse(this.message)