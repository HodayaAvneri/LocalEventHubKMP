package com.localeventhub.app.dashboard.domain.entity

import kotlinx.datetime.Clock

data class CommentEntity(val id: String,val text: String, val timestamp: Long = Clock.System.now().toEpochMilliseconds(), val user: UserEntity?)
