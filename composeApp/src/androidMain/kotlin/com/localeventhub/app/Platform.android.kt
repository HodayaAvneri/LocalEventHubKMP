package com.localeventhub.app

import android.os.Build

class AndroidPlatform : com.localeventhub.app.Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): com.localeventhub.app.Platform = com.localeventhub.app.AndroidPlatform()