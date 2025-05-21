package com.localeventhub.app

import android.app.Application
import com.localeventhub.app.di.initializeKoin
import org.koin.android.ext.koin.androidContext

class TemplateApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        com.localeventhub.app.expect.AppContext.setUp(applicationContext)
        initializeKoin(
            config = { androidContext(this@TemplateApplication) }
        )
        multiplatform.network.cmptoast.AppContext.apply { set(applicationContext) }

    }

}