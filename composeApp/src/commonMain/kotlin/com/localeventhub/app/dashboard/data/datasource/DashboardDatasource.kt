package com.localeventhub.app.dashboard.data.datasource

import com.localeventhub.app.dashboard.data.model.CommentDto
import com.localeventhub.app.dashboard.data.model.PostDto
import com.localeventhub.app.dashboard.data.model.UserDto


class DashboardDatasource(private val apiService: DashboardFirebaseService) {
    suspend fun updateUser(userDto: UserDto) = apiService.updateUser(userDto)
    suspend fun addPost(postDto: PostDto) = apiService.addPost(postDto)
    suspend fun getPosts() = apiService.getPosts()
    suspend fun getPost(id: String) = apiService.getPost(id)
    suspend fun updatePost(postDto: PostDto) = apiService.updatePost(postDto)
    suspend fun deletePost(id: String) = apiService.deletePost(id)
    suspend fun updateLike(id: String, likedUserList: MutableList<String>) = apiService.updateLike(id,likedUserList)
    suspend fun getComments(id: String) = apiService.getComments(id)
    suspend fun addComment(id: String, comment: CommentDto) = apiService.addComment(id,comment)
    suspend fun deleteComment(id: String, commentId: String) = apiService.deleteComment(id,commentId)
}