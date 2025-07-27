package com.localeventhub.app.dashboard.data.datasource

import com.localeventhub.app.dashboard.data.model.AddCommentResponseDto
import com.localeventhub.app.dashboard.data.model.CommentDto
import com.localeventhub.app.dashboard.data.model.PostDto
import com.localeventhub.app.dashboard.data.model.UpdateResponseDto
import com.localeventhub.app.dashboard.data.model.UserDto
import com.localeventhub.app.dashboard.domain.entity.CommentEntity
import com.localeventhub.app.expect.getFile
import com.localeventhub.app.featurebase.common.PreferenceStorage
import com.localeventhub.app.featurebase.common.PrefsDataStore
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseException
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.storage.storage
import kotlinx.datetime.Clock

class DashboardFirebaseService(private val dataStore: PrefsDataStore): DashboardDatasource {

    override suspend fun updateUser(userDto: UserDto): UpdateResponseDto {
        // Sign in using the provided email and password
        try {
            if (userDto.shouldUpdateProfile) {
                // Upload profile image and get the download URL
                val fileUri = getFile(userDto.profileImageUrl)
                val storageRef = Firebase.storage.reference
                    .child("images").child("users")
                    .child("${Clock.System.now().toEpochMilliseconds()}.jpg")

                storageRef.putFile(fileUri)

                val downloadUrl = storageRef.getDownloadUrl()
                userDto.profileImageUrl = downloadUrl
            }
            Firebase.firestore.collection("USERS")
                .document(userDto.userId)
                .update(
                    "email" to userDto.email,
                    "name" to userDto.name,
                    "profileImageUrl" to userDto.profileImageUrl,
                    "updatedAt" to userDto.updatedAt
                )

            PreferenceStorage.saveData(dataStore, "user_image", userDto.profileImageUrl)
            PreferenceStorage.saveData(dataStore, "user_name", userDto.name)
            PreferenceStorage.saveData(dataStore, "user_email", userDto.email)
            // Return success
            return UpdateResponseDto("User updated successfully")
        } catch (e: FirebaseException) {
            // Handle all possible exceptions
            return UpdateResponseDto("User update failed")
        }
    }

    override suspend fun addPost(postDto: PostDto): UpdateResponseDto {
        try {
            // Upload profile image and get the download URL
            val fileUri = getFile(postDto.imageUrl!!)
            val storageRef = Firebase.storage.reference
                .child("images").child("posts")
                .child("${Clock.System.now().toEpochMilliseconds()}.jpg")

            storageRef.putFile(fileUri)

            val downloadUrl = storageRef.getDownloadUrl()
            postDto.imageUrl = downloadUrl
            postDto.userId = PreferenceStorage.getData(dataStore, "user_id").toString()
            postDto.user = UserDto(
                postDto.userId,
                PreferenceStorage.getData(dataStore, "user_name").toString(),
                PreferenceStorage.getData(dataStore, "user_email").toString(),
                PreferenceStorage.getData(dataStore, "user_image").toString()
            )
            Firebase.firestore.collection("POSTS")
                .add(postDto)
            // Return success
            return UpdateResponseDto("Post added successfully")
        } catch (e: FirebaseException) {
            // Handle all possible exceptions
            return UpdateResponseDto("Post add failed")
        }
    }

    override suspend fun getPosts(): List<PostDto> {
        try {
            val response: List<PostDto> = Firebase.firestore.collection("POSTS").orderBy("timestamp",Direction.DESCENDING)
                .get().documents.map {
                    val data: PostDto = it.data()
                    data.copy(postId = it.id)
                }
            // Return success
            return response
        } catch (e: FirebaseException) {
            // Handle all possible exceptions
            return listOf()
        }
    }

    override suspend fun getPost(id: String): PostDto? {
        try {
            val response: PostDto = Firebase.firestore.collection("POSTS")
                .document(id)
                .get().data()
            // Return success
            return response
        } catch (e: FirebaseException) {
            // Handle all possible exceptions
            return null
        }
    }

    override suspend fun updatePost(postDto: PostDto): UpdateResponseDto {
        try {
            if (postDto.shouldUpdatePhoto) {
                // Upload profile image and get the download URL
                val fileUri = getFile(postDto.imageUrl!!)
                val storageRef = Firebase.storage.reference
                    .child("images").child("posts")
                    .child("${Clock.System.now().toEpochMilliseconds()}.jpg")

                storageRef.putFile(fileUri)

                val downloadUrl = storageRef.getDownloadUrl()
                postDto.imageUrl = downloadUrl
            }
            Firebase.firestore.collection("POSTS")
                .document(postDto.postId)
                .set(postDto)
            // Return success
            return UpdateResponseDto("Post updated successfully")
        } catch (e: FirebaseException) {
            // Handle all possible exceptions
            return UpdateResponseDto("Post updated failed")
        }
    }

    override suspend fun deletePost(id: String): UpdateResponseDto {
        try {
            Firebase.firestore.collection("POSTS")
                .document(id)
                .delete()
            return UpdateResponseDto("Post deleted successfully")
        } catch (e: FirebaseException) {
            // Handle all possible exceptions
            return UpdateResponseDto("Post delete failed")
        }
    }

    override suspend fun updateLike(id: String, likedUserList: MutableList<String>): UpdateResponseDto {
        try {
            Firebase.firestore.collection("POSTS")
                .document(id).update("likedBy" to likedUserList)
            return UpdateResponseDto("Post deleted successfully")
        } catch (e: FirebaseException) {
            // Handle all possible exceptions
            return UpdateResponseDto("Post delete failed")
        }
    }

    override suspend fun getComments(postId: String): List<CommentDto> {
        try {
            val response: List<CommentDto> = Firebase.firestore.collection("POSTS")
                .document(postId).collection("COMMENTS").orderBy("timestamp").get().documents.map {
                    val data: CommentDto = it.data()
                    data.copy(id = it.id)
                }
            // Return success
            return response
        } catch (e: FirebaseException) {
            // Handle all possible exceptions
            return listOf()
        }
    }

    override suspend fun addComment(id: String,commentDto: CommentDto): AddCommentResponseDto{
        try {
            val comment = commentDto.copy(userDto = UserDto(
                PreferenceStorage.getData(dataStore, "user_id").toString(),
                PreferenceStorage.getData(dataStore, "user_name").toString(),
                PreferenceStorage.getData(dataStore, "user_email").toString(),
                PreferenceStorage.getData(dataStore, "user_image").toString()
            ))
           val reference =  Firebase.firestore.collection("POSTS")
                .document(id).collection("COMMENTS").add(comment)
           comment.id = reference.id
           return AddCommentResponseDto(comment,"Comment added successfully")
        } catch (e: FirebaseException) {
            // Handle all possible exceptions
            return AddCommentResponseDto(null,"Comment failed")
        }
    }

    override suspend fun deleteComment(postId: String,commentId: String): UpdateResponseDto{
        try {
            Firebase.firestore.collection("POSTS")
                .document(postId).collection("COMMENTS").document(commentId).delete()
            return UpdateResponseDto("Comment deleted successfully")
        } catch (e: FirebaseException) {
            // Handle all possible exceptions
            return UpdateResponseDto("Comment delete failed")
        }
    }
}