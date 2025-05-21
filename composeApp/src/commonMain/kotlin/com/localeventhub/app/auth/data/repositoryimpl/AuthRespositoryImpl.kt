package com.localeventhub.app.auth.data.repositoryimpl

import com.localeventhub.app.auth.data.datasource.AuthDatasource
import com.localeventhub.app.auth.data.mapper.toDomain
import com.localeventhub.app.auth.data.mapper.toDto
import com.localeventhub.app.auth.domain.entity.AuthRequest
import com.localeventhub.app.auth.domain.entity.AuthResponse
import com.localeventhub.app.auth.domain.repository.AuthRepository

class AuthRespositoryImpl(private val dataSource: AuthDatasource) : AuthRepository {

    override suspend fun signIn(authRequest: AuthRequest): AuthResponse {
      return dataSource.signIn(authRequest.toDto()).toDomain()
    }

}