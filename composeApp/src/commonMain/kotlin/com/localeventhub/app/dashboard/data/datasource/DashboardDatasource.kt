package com.localeventhub.app.dashboard.data.datasource

import com.localeventhub.app.dashboard.data.model.AddCommentResponseDto
import com.localeventhub.app.dashboard.data.model.CommentDto
import com.localeventhub.app.dashboard.data.model.PostDto
import com.localeventhub.app.dashboard.data.model.UpdateResponseDto
import com.localeventhub.app.dashboard.data.model.UserDto


interface DashboardDatasource {
    suspend fun updateUser(userDto: UserDto): UpdateResponseDto
    suspend fun addPost(postDto: PostDto):UpdateResponseDto
    suspend fun getPosts(): List<PostDto>
    suspend fun getPost(id: String): PostDto?
    suspend fun updatePost(postDto: PostDto): UpdateResponseDto
    suspend fun deletePost(id: String): UpdateResponseDto
    suspend fun updateLike(id: String, likedUserList: MutableList<String>): UpdateResponseDto
    suspend fun getComments(id: String): List<CommentDto>
    suspend fun addComment(id: String, comment: CommentDto): AddCommentResponseDto
    suspend fun deleteComment(id: String, commentId: String): UpdateResponseDto
}