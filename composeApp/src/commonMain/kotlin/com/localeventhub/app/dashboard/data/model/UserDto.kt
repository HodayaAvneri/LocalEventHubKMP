package com.localeventhub.app.dashboard.data.model

import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable

@Serializable
class UserDto(var userId: String = "",
              var name: String = "",
              val email: String = "",
              var profileImageUrl: String = "",
              var shouldUpdateProfile : Boolean = false,
              val updatedAt: Long = Clock.System.now().toEpochMilliseconds()) {
}