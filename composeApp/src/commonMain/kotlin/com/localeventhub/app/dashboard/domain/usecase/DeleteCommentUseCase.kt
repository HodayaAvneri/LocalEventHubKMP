package com.localeventhub.app.dashboard.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import com.localeventhub.app.dashboard.domain.repository.DashboardRepository
import com.localeventhub.app.featurebase.common.ApiResult

class DeleteCommentUseCase(private val dashboardRepository: DashboardRepository) {

    operator fun invoke(id: String, commentId: String) = flow {
        emit(ApiResult.loading())
        val deleteCommentResponse = dashboardRepository.deleteComment(id,commentId)
        emit(ApiResult.success(deleteCommentResponse))
    }.catch {
        println(it.message)
        emit(ApiResult.error(null,code = 404, message = it.message ?: "Error Occurred"))
    }.flowOn(Dispatchers.IO)
}