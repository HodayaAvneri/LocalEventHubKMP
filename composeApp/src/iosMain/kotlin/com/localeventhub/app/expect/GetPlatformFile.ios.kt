package com.localeventhub.app.expect

import dev.gitlive.firebase.storage.File
import platform.Foundation.NSURL

actual fun getFile(path: String): File {
    return File(NSURL.fileURLWithPath(path))
}