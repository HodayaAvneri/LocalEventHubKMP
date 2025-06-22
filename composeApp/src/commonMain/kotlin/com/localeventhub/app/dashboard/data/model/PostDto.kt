package com.localeventhub.app.dashboard.data.model

import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable

@Serializable
data class PostDto(
    val postId: String,
    var userId: String,
    var description: String,
    var imageUrl: String?,
    var location: EventLocation?,
    val timestamp: Long = Clock.System.now().toEpochMilliseconds(),
    var likesCount: Int = 0,
    var likedBy: String = "[]",
    var tags: List<String> = listOf(),
    var user: UserDto?,
)