package com.localeventhub.app.expect

import kotlinx.cinterop.ExperimentalForeignApi
import com.localeventhub.app.featurebase.common.PrefsDataStore
import com.localeventhub.app.featurebase.common.createDataStore
import com.localeventhub.app.featurebase.common.dataStoreFileName
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual val targetModule: Module
    get() = module {
        //single { DatabaseDriverFactory().create() }
        single<PrefsDataStore> { createDataStore (
            producePath = {
                val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
                    directory = NSDocumentDirectory,
                    inDomain = NSUserDomainMask,
                    appropriateForURL = null,
                    create = false,
                    error = null,
                )
                requireNotNull(documentDirectory).path + "/$dataStoreFileName"
            },
        ) }
    }