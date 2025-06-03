package com.localeventhub.app.expect

import dev.gitlive.firebase.storage.File
import androidx.core.net.toUri

actual fun getFile(path: String): File {
    return File(path.toUri())
}