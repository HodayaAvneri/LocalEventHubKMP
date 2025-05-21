package com.localeventhub.app.auth.data.datasource

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import com.localeventhub.app.auth.data.model.AuthRequestDto
import com.localeventhub.app.auth.data.model.AuthResponseDto
import com.localeventhub.app.featurebase.common.PrefsDataStore

class AuthApiService(private val dataStore: PrefsDataStore, private val httpClient: HttpClient) {

    suspend fun signIn(authRequestDto: AuthRequestDto): AuthResponseDto{
        val response = httpClient.post(com.localeventhub.app.featurebase.common.APIConstants.BASE_URL.plus("")) {
            contentType(ContentType.Application.Json)
            setBody(authRequestDto)
        }
        return if(response.status == HttpStatusCode.OK) {
            val responseBody: AuthResponseDto = response.body()
            if(!responseBody.accessToken.isNullOrEmpty()){
                //TokenStorage.saveTokens(dataStore, responseBody.accessToken, responseBody.refreshToken!!)
            }
            responseBody
        } else AuthResponseDto(null,null,message = "SignIn Error. Please try again!!!")
    }
}