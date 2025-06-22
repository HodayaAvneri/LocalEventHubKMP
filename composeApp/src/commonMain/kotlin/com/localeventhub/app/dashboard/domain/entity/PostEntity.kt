package com.localeventhub.app.dashboard.domain.entity

import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable

@Serializable
data class PostEntity(
    val postId: String,
    val userId: String,
    var description: String,
    var imageUrl: String?,
    var location: EventLocation?,
    val timestamp: Long = Clock.System.now().toEpochMilliseconds(),
    var likesCount: Int = 0,
    var likedBy: String = "[]",
    var tags: List<String> = listOf(),
    val user: UserEntity?,
)