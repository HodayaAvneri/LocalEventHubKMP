package com.localeventhub.app.dashboard.domain.repository

import com.localeventhub.app.dashboard.domain.entity.PostEntity
import com.localeventhub.app.dashboard.domain.entity.UpdateResponse
import com.localeventhub.app.dashboard.domain.entity.UserEntity


interface DashboardRepository {

    suspend fun updateUser(userEntity: UserEntity): UpdateResponse

    suspend fun addPost(post: PostEntity): UpdateResponse

    suspend fun getAllPost(): List<PostEntity>

    suspend fun getPost(id: String): PostEntity?
}