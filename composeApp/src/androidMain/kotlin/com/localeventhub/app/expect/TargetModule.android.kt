package com.localeventhub.app.expect

import com.localeventhub.app.featurebase.common.PrefsDataStore
import com.localeventhub.app.featurebase.common.createDataStore
import com.localeventhub.app.featurebase.common.dataStoreFileName
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val targetModule: Module
    get() = module {
        //single { DatabaseDriverFactory(androidContext()).create() }
        single<PrefsDataStore> {
            createDataStore(producePath = {
                androidContext().filesDir.resolve(dataStoreFileName).absolutePath
            })
        }
    }