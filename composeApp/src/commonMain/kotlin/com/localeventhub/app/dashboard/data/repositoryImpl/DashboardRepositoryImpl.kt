package com.localeventhub.app.dashboard.data.repositoryImpl

import com.localeventhub.app.dashboard.data.datasource.DashboardDatasource
import com.localeventhub.app.dashboard.data.mapper.toDomain
import com.localeventhub.app.dashboard.data.mapper.toDto
import com.localeventhub.app.dashboard.domain.entity.AddCommentResponseEntity
import com.localeventhub.app.dashboard.domain.entity.CommentEntity
import com.localeventhub.app.dashboard.domain.entity.PostEntity
import com.localeventhub.app.dashboard.domain.entity.UpdateResponse
import com.localeventhub.app.dashboard.domain.repository.DashboardRepository

class DashboardRepositoryImpl(private val dataSource: DashboardDatasource) : DashboardRepository {

    override suspend fun updateUser(userEntity: com.localeventhub.app.dashboard.domain.entity.UserEntity): UpdateResponse {
        return dataSource.updateUser(userEntity.toDto()).toDomain()
    }

    override suspend fun addPost(post: PostEntity): UpdateResponse {
        return dataSource.addPost(post.toDto()).toDomain()
    }

    override suspend fun getAllPost(): List<PostEntity> {
        return dataSource.getPosts().map { it.toDomain()}
    }

    override suspend fun getPost(id: String): PostEntity? {
        return dataSource.getPost(id)?.toDomain()
    }

    override suspend fun deletePost(id: String): UpdateResponse {
        return dataSource.deletePost(id).toDomain()
    }

    override suspend fun updatePost(post: PostEntity): UpdateResponse {
        return dataSource.updatePost(post.toDto()).toDomain()
    }

    override suspend fun updateLike(id: String, likedUserList: MutableList<String>): UpdateResponse {
        return dataSource.updateLike(id, likedUserList).toDomain()
    }

    override suspend fun getComments(id: String): List<CommentEntity> {
        return dataSource.getComments(id).map { it.toDomain() }
    }

    override suspend fun addComment(id: String, commentEntity: CommentEntity): AddCommentResponseEntity {
        return dataSource.addComment(id,commentEntity.toDto()).toDomain()
    }

    override suspend fun deleteComment(id: String, commentId: String): UpdateResponse {
        return dataSource.deleteComment(id,commentId).toDomain()
    }
}