package com.localeventhub.app.dashboard.data.mapper

import com.localeventhub.app.dashboard.data.model.EventLocation
import com.localeventhub.app.dashboard.data.model.PostDto
import com.localeventhub.app.dashboard.domain.entity.PostEntity

fun PostEntity.toDto() =  PostDto(this.postId, this.userId, this.description, this.imageUrl, EventLocation(location?.latitude!!,location?.longitude!!,location?.address!!), this.timestamp, this.likesCount, this.likedBy, this.tags,this.user?.toDto())

fun PostDto.toDomain() =  PostEntity(this.postId, this.userId, this.description, this.imageUrl, com.localeventhub.app.dashboard.domain.entity.EventLocation(location?.latitude!!,location?.longitude!!,location?.address!!), this.timestamp, this.likesCount, this.likedBy, this.tags, this.user?.toDomain())