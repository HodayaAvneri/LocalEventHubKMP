package com.localeventhub.app.featurebase.common

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


object NetworkClient {

    fun createClient(dataStore: PrefsDataStore): HttpClient {
        return HttpClient {
            install(Logging){
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                })
            }
            install(Auth) {
                /*bearer {
                    loadTokens {
                        TokenStorage.getAccessToken(dataStore)?.let { accessToken ->
                            TokenStorage.getRefreshToken(dataStore)?.let { refreshToken ->
                                BearerTokens(accessToken, refreshToken)
                            }
                        }
                    }
                    refreshTokens {
                        val refreshToken = TokenStorage.getRefreshToken(dataStore)
                        refreshToken?.let {
                            try {
                                val tokenResponse = this.client.post(
                                    APIConstants.BASE_URL.plus(
                                        APIConstants.REFRESH_TOKEN)) {
                                    markAsRefreshTokenRequest()
                                    contentType(ContentType.Application.Json)
                                    setBody(mapOf("refreshToken" to refreshToken))
                                }.body<TokenResponse>()
                                TokenStorage.saveTokens(
                                    dataStore,
                                    tokenResponse.accessToken,
                                    tokenResponse.refreshToken
                                )
                                BearerTokens(tokenResponse.accessToken, tokenResponse.refreshToken)
                            } catch (e: Exception) {
                                // Handle refresh failure (e.g., logout)
                                TokenStorage.clearTokens(dataStore)
                                null
                            }
                        }
                    }
                }*/
            }
        }
    }
}