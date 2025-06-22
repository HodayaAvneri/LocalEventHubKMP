package com.localeventhub.app.dashboard.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import com.localeventhub.app.auth.domain.entity.AuthRequest
import com.localeventhub.app.auth.domain.repository.AuthRepository
import com.localeventhub.app.dashboard.domain.entity.UserEntity
import com.localeventhub.app.dashboard.domain.repository.DashboardRepository
import com.localeventhub.app.featurebase.common.ApiResult

class UpdateUserUseCase(private val dashboardRepository: DashboardRepository) {

    operator fun invoke(userRequest: UserEntity) = flow {
        emit(ApiResult.loading())
        val signInResponse = dashboardRepository.updateUser(userRequest)
        emit(ApiResult.success(signInResponse))
    }.catch {
        println(it.message)
        emit(ApiResult.error(null,code = 404, message = it.message ?: "Error Occurred"))
    }.flowOn(Dispatchers.IO)
}