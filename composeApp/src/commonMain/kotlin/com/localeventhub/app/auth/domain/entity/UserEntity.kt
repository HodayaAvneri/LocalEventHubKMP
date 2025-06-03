package com.localeventhub.app.auth.domain.entity

import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable

class UserEntity(var userId: String = "",
                 var name: String = "",
                 val email: String = "",
                 var profileImageUrl: String = "",
                 val createdAt: Long =
                  Clock.System.now().toEpochMilliseconds(),
                 val updatedAt: Long = Clock.System.now().toEpochMilliseconds()) {
}