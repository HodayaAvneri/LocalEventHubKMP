package com.localeventhub.app.featurebase.common

data class ApiResult<out T>(val code: Int = 200, val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(
            data: T
        ): ApiResult<T> = ApiResult(status = Status.SUCCESS, data = data, message = null)

        fun <T> error(
            data: T?,
            code: Int,
            message: String
        ): ApiResult<T> = ApiResult(code = code, status = Status.ERROR, data = data, message = message)

        fun <T> loading(): ApiResult<T> = ApiResult(status = Status.LOADING, data = null, message = null)
    }
}
