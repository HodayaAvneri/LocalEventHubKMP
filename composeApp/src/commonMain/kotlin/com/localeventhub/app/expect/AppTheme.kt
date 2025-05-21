package com.localeventhub.app.expect

import androidx.compose.runtime.Composable

@Composable
expect fun AppTheme(darkTheme: Boolean,
                    dynamicColor: Boolean,
                    content: @Composable () -> Unit)

expect fun isApiLevel35(): Boolean