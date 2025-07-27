package com.localeventhub.app.dashboard.domain.repository

import com.localeventhub.app.dashboard.domain.entity.AddCommentResponseEntity
import com.localeventhub.app.dashboard.domain.entity.CommentEntity
import com.localeventhub.app.dashboard.domain.entity.PostEntity
import com.localeventhub.app.dashboard.domain.entity.UpdateResponse
import com.localeventhub.app.dashboard.domain.entity.UserEntity


interface DashboardRepository {

    suspend fun updateUser(userEntity: UserEntity): UpdateResponse

    suspend fun addPost(post: PostEntity): UpdateResponse

    suspend fun getAllPost(): List<PostEntity>

    suspend fun getPost(id: String): PostEntity?

    suspend fun deletePost(id: String): UpdateResponse

    suspend fun updatePost(post: PostEntity): UpdateResponse

    suspend fun updateLike(id: String, likedUserList: MutableList<String>): UpdateResponse

    suspend fun getComments(id: String): List<CommentEntity>

    suspend fun addComment(id: String,commentEntity: CommentEntity): AddCommentResponseEntity

    suspend fun deleteComment(id: String,commentId: String): UpdateResponse

}