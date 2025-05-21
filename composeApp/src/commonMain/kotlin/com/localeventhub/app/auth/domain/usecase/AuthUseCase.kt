package com.localeventhub.app.auth.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import com.localeventhub.app.auth.domain.entity.AuthRequest
import com.localeventhub.app.auth.domain.repository.AuthRepository
import com.localeventhub.app.featurebase.common.ApiResult
import com.localeventhub.app.featurebase.common.getErrorMessageFromBody

class AuthUseCase(private val authRepository: AuthRepository) {

    operator fun invoke(authRequest: AuthRequest) = flow {
        emit(ApiResult.loading())
        val todoResponse = authRepository.signIn(authRequest)
        emit(ApiResult.success(todoResponse))
    }.catch {
        println(it.message)
        val error = getErrorMessageFromBody(it)
        emit(ApiResult.error(null,code = error.first, message = error.second))
    }.flowOn(Dispatchers.IO)
}