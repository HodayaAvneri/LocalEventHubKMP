package com.localeventhub.app.dashboard.presentation.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.localeventhub.app.dashboard.domain.entity.PostEntity
import com.localeventhub.app.expect.GoogleMap
import com.localeventhub.app.expect.LatLng
import com.localeventhub.app.featurebase.common.Status
import com.localeventhub.app.featurebase.common.UIStatus
import com.localeventhub.app.featurebase.common.Utils
import com.localeventhub.app.featurebase.presentation.ui.compose.LocationTracking
import com.localeventhub.app.featurebase.presentation.ui.state.UIState
import dev.jordond.compass.Location
import multiplatform.network.cmptoast.showToast
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MapScreen(paddingValues: () -> PaddingValues, viewModel: MapViewModel = koinViewModel()) {

    val getAllPostResponse by viewModel.getEventListResponse.collectAsState(initial = null)
    var uiState by remember { mutableStateOf<UIState<List<PostEntity>?>>(UIState.initial()) }
    var getCurrentLocation by remember { mutableStateOf(false) }
    val utils = remember { Utils() }
    val postList = remember {
        mutableStateListOf<PostEntity>()
    }
    var markerDatum by remember { mutableStateOf(MarkerDatum()) }
    var title by remember { mutableStateOf("") }

    LaunchedEffect(Unit){
        viewModel.getAllPost()
    }
    LaunchedEffect(getAllPostResponse?.status) {
        when (getAllPostResponse?.status) {
            Status.SUCCESS -> {
                val response = getAllPostResponse?.data
                response?.let {
                    postList.clear()
                    postList.addAll(response)
                }
                uiState = UIState.success(response)
                if(postList.isNotEmpty()){
                    getCurrentLocation = true
                }else{
                    showToast("No Events Found")
                }
            }

            Status.ERROR -> {
                uiState = UIState.error(getAllPostResponse?.message ?: "")
                viewModel.resetState()
            }

            Status.LOADING -> {
                uiState = UIState.loading()
            }

            null -> {}
        }
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(paddingValues()),
        verticalArrangement = Arrangement.Center
    ) {
        when (uiState.status) {
            UIStatus.SUCCESS -> {
                if (getCurrentLocation) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                    LocationTracking { location: Location ->
                        println(location)
                        val sortedList = postList.filter { it.location != null }.sortedBy { post ->
                            val loc = post.location!!
                            utils.haversineDistance(location.coordinates.latitude, location.coordinates.longitude, loc.latitude, loc.longitude)
                        }
                        postList.clear()
                        postList.addAll(sortedList)
                        title = postList[0].description
                        markerDatum = MarkerDatum(LatLng(location.coordinates.latitude, location.coordinates.longitude),
                            LatLng(postList[0].location?.latitude!!, postList[0].location?.longitude!!))
                        getCurrentLocation = false
                    }
                } else {
                    markerDatum.markerLocation?.let {
                        GoogleMap(
                            currentLocationPosition = markerDatum.currentLocation !!,
                            markerPosition = markerDatum.markerLocation !!,
                            title = title
                        )
                    }
                }
            }

            UIStatus.LOADING -> {
                CircularProgressIndicator()
            }

            else -> {}
        }
    }
}

data class MarkerDatum(val currentLocation: LatLng?=null, val markerLocation: LatLng?=null)

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
