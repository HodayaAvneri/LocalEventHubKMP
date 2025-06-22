package com.localeventhub.app.dashboard.data.datasource

import com.localeventhub.app.dashboard.data.model.UpdateResponseDto
import com.localeventhub.app.dashboard.data.model.UserDto
import com.localeventhub.app.expect.getFile
import com.localeventhub.app.featurebase.common.PreferenceStorage
import com.localeventhub.app.featurebase.common.PrefsDataStore
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseException
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.storage.storage
import kotlinx.datetime.Clock

class DashboardFirebaseService(private val dataStore: PrefsDataStore) {

    suspend fun updateUser(userDto: UserDto): UpdateResponseDto {
        // Sign in using the provided email and password
        try {
            if(userDto.shouldUpdateProfile){
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
                .update("email" to userDto.email, "name" to userDto.name, "profileImageUrl" to userDto.profileImageUrl, "updatedAt" to userDto.updatedAt)

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

}