package com.localeventhub.app.dashboard.data.model

import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class UserDto(var userId: String = "",
              var name: String = "",
              val email: String = "",
              var profileImageUrl: String = "",
              @Transient var shouldUpdateProfile : Boolean = false,
              val updatedAt: Long = Clock.System.now().toEpochMilliseconds()) {
}