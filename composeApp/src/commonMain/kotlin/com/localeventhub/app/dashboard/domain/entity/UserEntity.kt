package com.localeventhub.app.dashboard.domain.entity

import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable

@Serializable
class UserEntity(var userId: String = "",
                 var name: String = "",
                 val email: String = "",
                 var profileImageUrl: String = "",
                 var shouldUpdateProfile : Boolean = false,
                 val updatedAt: Long = Clock.System.now().toEpochMilliseconds())