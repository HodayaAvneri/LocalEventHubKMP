package com.localeventhub.app.expect

import androidx.compose.runtime.Composable
import com.localeventhub.app.featurebase.common.PrefsDataStore

@Composable
expect fun rememberDataStore(): PrefsDataStore