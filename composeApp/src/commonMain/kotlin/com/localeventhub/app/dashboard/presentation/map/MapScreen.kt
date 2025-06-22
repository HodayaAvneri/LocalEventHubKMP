package com.localeventhub.app.dashboard.presentation.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.localeventhub.app.expect.GoogleMap
import com.localeventhub.app.expect.LatLng

@Composable
fun MapScreen(paddingValues: () -> PaddingValues){

    Column(modifier = Modifier.fillMaxSize().padding(paddingValues()), verticalArrangement = Arrangement.Center) {
        GoogleMap(currentLocationPosition = LatLng(51.509865,-0.118092), markerPosition = LatLng(51.509865,-0.118092))
    }
}

/*suspend fun getUsers(): List<Post> {
    val firebaseFirestore = Firebase.firestore
    try {
        val userResponse =
            firebaseFirestore.collection("POSTS").get()
        return userResponse.documents.map { it.data() }
    } catch (e: Exception) {
        throw e
    }
}*/
