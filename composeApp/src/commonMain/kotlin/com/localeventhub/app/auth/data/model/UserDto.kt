package com.localeventhub.app.auth.data.model

import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable

@Serializable
class UserDto(var userId: String = "",
              var name: String = "",
              val email: String = "",
              var profileImageUrl: String = "",
              val createdAt: Long =
                  Clock.System.now().toEpochMilliseconds(),
              val updatedAt: Long = Clock.System.now().toEpochMilliseconds()) {
}