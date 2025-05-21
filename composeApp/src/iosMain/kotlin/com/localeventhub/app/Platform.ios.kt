package com.localeventhub.app

import platform.UIKit.UIDevice

class IOSPlatform: com.localeventhub.app.Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): com.localeventhub.app.Platform = IOSPlatform()