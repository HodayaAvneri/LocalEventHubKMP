package com.localeventhub.app.dashboard.domain.entity

import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class PostEntity(
    val postId: String,
    val userId: String,
    var description: String,
    var imageUrl: String?,
    var location: EventLocationEntity?,
    val timestamp: Long = Clock.System.now().toEpochMilliseconds(),
    var likesCount: Int = 0,
    var likedBy: MutableList<String> = mutableListOf(),
    var tags: List<String> = listOf(),
    val user: UserEntity?,
    @Transient var shouldUpdatePhoto: Boolean = false
)