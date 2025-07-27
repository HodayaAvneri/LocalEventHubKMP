package com.localeventhub.app.dashboard.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import com.localeventhub.app.dashboard.domain.entity.PostEntity
import com.localeventhub.app.dashboard.domain.repository.DashboardRepository
import com.localeventhub.app.featurebase.common.ApiResult

class DeletePostUseCase(private val dashboardRepository: DashboardRepository) {

    operator fun invoke(id: String) = flow {
        emit(ApiResult.loading())
        val deleteResponse = dashboardRepository.deletePost(id)
        emit(ApiResult.success(deleteResponse))
    }.catch {
        println(it.message)
        emit(ApiResult.error(null,code = 404, message = it.message ?: "Error Occurred"))
    }.flowOn(Dispatchers.IO)
}