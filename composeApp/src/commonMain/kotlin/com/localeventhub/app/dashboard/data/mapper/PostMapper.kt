package com.localeventhub.app.dashboard.data.mapper

import com.localeventhub.app.dashboard.data.model.EventLocationDto
import com.localeventhub.app.dashboard.data.model.PostDto
import com.localeventhub.app.dashboard.domain.entity.PostEntity

fun PostEntity.toDto() =  PostDto(this.postId, this.userId, this.description, this.imageUrl, EventLocationDto(location?.latitude!!,location?.longitude!!,location?.address!!), this.timestamp, this.likesCount, this.likedBy, this.tags,this.user?.toDto())

fun PostDto.toDomain() =  PostEntity(this.postId, this.userId, this.description, this.imageUrl, com.localeventhub.app.dashboard.domain.entity.EventLocationEntity(location?.latitude!!,location?.longitude!!,location?.address!!), this.timestamp, this.likesCount, this.likedBy, this.tags, this.user?.toDomain())