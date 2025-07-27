package com.localeventhub.app.dashboard.data.model

import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable

@Serializable
data class CommentDto(
    var id: String,
    val text: String,
    val timestamp: Long = Clock.System.now().toEpochMilliseconds(),
    val userDto: UserDto?
)
