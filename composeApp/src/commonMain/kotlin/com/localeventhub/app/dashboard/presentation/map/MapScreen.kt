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
fun MapScreen(
    paddingValues: () -> PaddingValues,
    viewModel: MapViewModel = koinViewModel()
) {
    val getAllPostResponse by viewModel.getEventListResponse.collectAsState(initial = null)
    var uiState by remember { mutableStateOf<UIState<List<PostEntity>?>>(UIState.initial()) }
    var waitingForLocation by remember { mutableStateOf(false) }
    val utils = remember { Utils() }
    val postList = remember { mutableStateListOf<PostEntity>() }
    val markerList = remember { mutableStateListOf<Pair<LatLng, String>>() }
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }

    LaunchedEffect(Unit) { viewModel.getAllPost() }

    LaunchedEffect(getAllPostResponse?.status) {
        when (getAllPostResponse?.status) {
            Status.SUCCESS -> {
                postList.clear()
                getAllPostResponse?.data?.let(postList::addAll)
                uiState = UIState.success(postList)
                if (postList.isNotEmpty()) waitingForLocation = true else showToast("No Events Found")
            }

            Status.ERROR -> {
                uiState = UIState.error(getAllPostResponse?.message ?: "")
                viewModel.resetState()
            }

            Status.LOADING -> uiState = UIState.loading()
            null -> {}
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(paddingValues()),
        verticalArrangement = Arrangement.Center
    ) {
        when (uiState.status) {
            UIStatus.SUCCESS -> {
                if (waitingForLocation) {
                    Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
                    LocationTracking { location: Location ->
                        currentLocation = LatLng(location.coordinates.latitude, location.coordinates.longitude)
                        markerList.clear()
                        postList
                            .filter { it.location != null }
                            .sortedBy { post ->
                                val loc = post.location!!
                                utils.haversineDistance(
                                    location.coordinates.latitude,
                                    location.coordinates.longitude,
                                    loc.latitude,
                                    loc.longitude
                                )
                            }
                            .forEach { post ->
                                markerList.add(
                                    LatLng(post.location!!.latitude, post.location!!.longitude) to post.description
                                )
                            }
                        waitingForLocation = false
                    }
                } else {
                    if (currentLocation != null && markerList.isNotEmpty()) {
                        GoogleMap(
                            currentLocationPosition = currentLocation!!,
                            markerPositions = markerList
                        )
                    }
                }
            }

            UIStatus.LOADING -> CircularProgressIndicator()
            else -> {}
        }
    }
}
