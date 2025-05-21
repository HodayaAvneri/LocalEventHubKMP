package com.localeventhub.app

class Greeting {
    private val platform = com.localeventhub.app.getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}