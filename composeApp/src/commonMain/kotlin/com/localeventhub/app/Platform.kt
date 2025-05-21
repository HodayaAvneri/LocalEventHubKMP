package com.localeventhub.app

interface Platform {
    val name: String
}

expect fun getPlatform(): com.localeventhub.app.Platform