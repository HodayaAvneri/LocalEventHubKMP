package com.localeventhub.app.auth.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import com.localeventhub.app.auth.domain.entity.AuthRequest
import com.localeventhub.app.auth.domain.repository.AuthRepository
import com.localeventhub.app.featurebase.common.ApiResult

class SignInUseCase(private val authRepository: AuthRepository) {

    operator fun invoke(authRequest: AuthRequest) = flow {
        emit(ApiResult.loading())
        val signInResponse = authRepository.signIn(authRequest)
        emit(ApiResult.success(signInResponse))
    }.catch {
        println(it.message)
        emit(ApiResult.error(null,code = 404, message = it.message ?: "Error Occurred"))
    }.flowOn(Dispatchers.IO)
}