package com.localeventhub.app.dashboard.data.repositoryImpl

import com.localeventhub.app.dashboard.data.datasource.DashboardDatasource
import com.localeventhub.app.dashboard.data.mapper.toDomain
import com.localeventhub.app.dashboard.data.mapper.toDto
import com.localeventhub.app.dashboard.domain.entity.UpdateResponse
import com.localeventhub.app.dashboard.domain.repository.DashboardRepository

class DashboardRepositoryImpl(private val dataSource: DashboardDatasource) : DashboardRepository {
    override suspend fun updateUser(userEntity: com.localeventhub.app.dashboard.domain.entity.UserEntity): UpdateResponse {
        return dataSource.updateUser(userEntity.toDto()).toDomain()
    }
}