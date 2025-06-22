package com.localeventhub.app.dashboard.data.datasource

import com.localeventhub.app.dashboard.data.model.UserDto


class DashboardDatasource(private val apiService: DashboardFirebaseService) {
    suspend fun updateUser(userDto: UserDto) = apiService.updateUser(userDto)
}