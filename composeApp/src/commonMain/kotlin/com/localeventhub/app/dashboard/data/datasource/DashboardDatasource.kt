package com.localeventhub.app.dashboard.data.datasource

import com.localeventhub.app.dashboard.data.model.PostDto
import com.localeventhub.app.dashboard.data.model.UserDto


class DashboardDatasource(private val apiService: DashboardFirebaseService) {
    suspend fun updateUser(userDto: UserDto) = apiService.updateUser(userDto)
    suspend fun addPost(postDto: PostDto) = apiService.addPost(postDto)
    suspend fun getPosts() = apiService.getPosts()
    suspend fun getPost(id: String) = apiService.getPost(id)
}