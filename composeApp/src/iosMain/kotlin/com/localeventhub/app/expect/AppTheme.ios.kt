package com.localeventhub.app.expect

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.localeventhub.app.featurebase.presentation.theme.darkScheme
import com.localeventhub.app.featurebase.presentation.theme.lightScheme

@Composable
actual fun AppTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if(darkTheme) darkScheme else lightScheme,
        content = content
    )
}

actual fun isApiLevel35() = false