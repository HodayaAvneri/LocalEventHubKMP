package com.localeventhub.app.di
import com.localeventhub.app.auth.di.authDIModule
import com.localeventhub.app.dashboard.di.dashboardDIModule
import com.localeventhub.app.expect.targetModule
import com.localeventhub.app.featurebase.common.NetworkClient
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module

val sharedModule = module {
    single {
        NetworkClient.createClient(get())
    }
}

fun initializeKoin(config: (KoinApplication.() -> Unit)? = null) {
    startKoin {
        config?.invoke(this)
        modules(targetModule, sharedModule, authDIModule, dashboardDIModule)
    }
}

