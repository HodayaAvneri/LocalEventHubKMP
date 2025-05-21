package com.localeventhub.app.auth.data.datasource

import com.localeventhub.app.auth.data.model.AuthRequestDto

class AuthDatasource(private val apiService: AuthApiService) {

    suspend fun signIn(authRequestDto: AuthRequestDto) = apiService.signIn(authRequestDto)
}