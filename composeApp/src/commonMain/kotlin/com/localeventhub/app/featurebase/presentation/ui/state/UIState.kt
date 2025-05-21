package com.localeventhub.app.featurebase.presentation.ui.state

import com.localeventhub.app.featurebase.common.UIStatus


data class UIState<out T>(val status: UIStatus, val data: T?, val message: String?) {
    companion object {
        fun <T> success(
            data: T
        ): UIState<T> = UIState(status = UIStatus.SUCCESS ,data = data, message = null)

        fun <T> error(
            message: String
        ): UIState<T> = UIState(status = UIStatus.ERROR ,data = null, message = message)

        fun <T> loading(): UIState<T> = UIState(status = UIStatus.LOADING, data = null, message = null)
        fun <T> initial(): UIState<T> = UIState(status = UIStatus.INITIAL, data = null, message = null)
    }
}