package com.localeventhub.app.expect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.localeventhub.app.featurebase.common.PrefsDataStore
import com.localeventhub.app.featurebase.common.createDataStore
import com.localeventhub.app.featurebase.common.dataStoreFileName

@Composable
actual fun rememberDataStore(): PrefsDataStore {
    val context = LocalContext.current
    return remember {
        createDataStore(
            producePath = {
                context.filesDir.resolve(dataStoreFileName).absolutePath
            },
        )
    }
}