package com.localeventhub.app.featurebase.common

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject


suspend fun getErrorMessageFromBody(error: Throwable): Pair<Int,String> {
    var code = 0
    var message = "Error Occurred"
    try {
        when(error){
            is ClientRequestException, is ServerResponseException -> {
                val responseException = error as ResponseException
                code = responseException.response.status.value
                message = when (val json = Json.parseToJsonElement(responseException.response.bodyAsText())) {
                    is JsonObject -> {
                        json["message"]?.toString()?.removeSurrounding("\"")
                            ?: json["error"]?.toString()?.removeSurrounding("\"")
                            ?: "Error occurred. Please try again"
                    }
                    is JsonArray -> {
                        // Handle array responses if needed
                        "Error occurred. Please try again}"
                    }
                    else -> "Error occurred. Please try again"
                }
            }
        }
        return Pair(code,message)
    }catch (e: Exception){
        e.printStackTrace()
       return Pair(code,message)
    }
}
