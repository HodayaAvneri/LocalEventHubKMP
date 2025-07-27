package com.localeventhub.app.auth.data.datasource

import androidx.datastore.preferences.core.edit
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import com.localeventhub.app.auth.data.model.AuthRequestDto
import com.localeventhub.app.auth.data.model.AuthResponseDto
import com.localeventhub.app.auth.data.model.UserDto
import com.localeventhub.app.expect.getFile
import com.localeventhub.app.featurebase.common.PreferenceStorage
import com.localeventhub.app.featurebase.common.PrefsDataStore
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseException
import dev.gitlive.firebase.app
import dev.gitlive.firebase.auth.AuthCredential
import dev.gitlive.firebase.auth.EmailAuthProvider
import dev.gitlive.firebase.auth.FirebaseAuthUserCollisionException
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.storage.File
import dev.gitlive.firebase.storage.storage
import kotlinx.datetime.Clock

class AuthFirebaseService(private val dataStore: PrefsDataStore): AuthDatasource {

    override suspend fun signUp(authRequestDto: AuthRequestDto, userDto: UserDto): AuthResponseDto {
        // Sign in using the provided email and password
        try {
            val authResult = Firebase.auth.createUserWithEmailAndPassword(
                authRequestDto.email,
                authRequestDto.password
            )

            val firebaseUser = authResult.user
                ?: return AuthResponseDto(null, "Authentication failed: User is null")

            // Upload profile image and get the download URL
            val fileUri = getFile(userDto.profileImageUrl)
            val storageRef = Firebase.storage.reference
                .child("images").child("users")
                .child("${Clock.System.now().toEpochMilliseconds()}.jpg")

            storageRef.putFile(fileUri)

            val downloadUrl = storageRef.getDownloadUrl()
            userDto.profileImageUrl = downloadUrl
            userDto.userId = firebaseUser.uid
            // Save user data to Firestore
            Firebase.firestore.collection("USERS")
                .document(firebaseUser.uid)
                .set(userDto)
            PreferenceStorage.saveData(dataStore, "user_id", firebaseUser.uid)
            PreferenceStorage.saveData(dataStore, "user_image", downloadUrl)
            PreferenceStorage.saveData(dataStore, "user_name", userDto.name)
            PreferenceStorage.saveData(dataStore, "user_email", userDto.email)
            // Return success
            return AuthResponseDto(firebaseUser.uid, "")
        } catch (e: FirebaseAuthUserCollisionException) {
            return AuthResponseDto(null, "User already exists")
        } catch (e: FirebaseException) {
            // Handle all possible exceptions
            return AuthResponseDto(null, e.message ?: "Authentication failed")
        }

    }

    override suspend fun signIn(authRequestDto: AuthRequestDto): AuthResponseDto {
        // Sign in using the provided email and password
        try {
            val authResult = Firebase.auth.signInWithCredential(
                EmailAuthProvider.credential(
                    authRequestDto.email,
                    authRequestDto.password
                )
            )
            val firebaseUser = authResult.user
                ?: return AuthResponseDto(null, "Authentication failed: User is null")
            val userDto: UserDto = Firebase.firestore.collection("USERS")
                .document(firebaseUser.uid)
                .get().data()
            PreferenceStorage.saveData(dataStore, "user_id", firebaseUser.uid)
            PreferenceStorage.saveData(dataStore, "user_image", userDto.profileImageUrl)
            PreferenceStorage.saveData(dataStore, "user_name", userDto.name)
            PreferenceStorage.saveData(dataStore, "user_email", userDto.email)

            // Return success
            return AuthResponseDto(firebaseUser.uid, "")
        } catch (e: FirebaseException) {
            // Handle all possible exceptions
            return AuthResponseDto(null,  "Authentication failed")
        }
    }

}