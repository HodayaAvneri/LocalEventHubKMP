package com.localeventhub.app.dashboard.domain

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val description: String = "",
    val imageUrl: String = "",
    val likedBy: List<String> = emptyList(),
    val likedByList: List<String> = emptyList(), // Optional, seems redundant with likedBy
    val likesCount: Int = 0,
    val location: Location? = null,
    val postId: String = "",
    val tags: List<String> = emptyList(),
    val timestamp: Long = 0L,
    val user: User? = null,
    val userId: String = ""
)

@Serializable
data class Location(
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

@Serializable
data class User(
    val createdAt: Long = 0L,
    val email: String = "",
    val name: String = "",
    val profileImageUrl: String = "",
    val updatedAt: Long = 0L,
    val userId: String = ""
)