package com.localeventhub.app.dashboard.domain.repository

import com.localeventhub.app.dashboard.domain.entity.UpdateResponse
import com.localeventhub.app.dashboard.domain.entity.UserEntity


interface DashboardRepository {

    suspend fun updateUser(userEntity: UserEntity): UpdateResponse
}