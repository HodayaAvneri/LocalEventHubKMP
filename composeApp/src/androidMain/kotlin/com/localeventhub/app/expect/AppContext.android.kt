package com.localeventhub.app.expect

import android.app.Application
import android.content.Context

actual object AppContext{
    private lateinit var application: Application

    fun setUp(context: Context) {
        com.localeventhub.app.expect.AppContext.application = context as Application
    }

    fun get(): Context {
        if (com.localeventhub.app.expect.AppContext::application.isInitialized.not()) throw Exception("Application context isn't initialized")
        return com.localeventhub.app.expect.AppContext.application.applicationContext
    }
}