package com.localeventhub.app.expect

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng as GmsLatLng
import com.google.maps.android.compose.GoogleMap as ComposeGoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
actual fun GoogleMap(
    modifier: Modifier,
    currentLocationPosition: LatLng,
    markerPositions: List<Pair<LatLng, String>>,
    onMapClick: (LatLng) -> Unit
) {
    val cameraState = rememberCameraPositionState()

    LaunchedEffect(markerPositions) {
        val target = markerPositions.firstOrNull()?.first ?: currentLocationPosition
        cameraState.animate(
            CameraUpdateFactory.newLatLngZoom(
                GmsLatLng(target.latitude, target.longitude),
                14f
            )
        )
    }

    ComposeGoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraState,
        onMapClick = { ll -> onMapClick(LatLng(ll.latitude, ll.longitude)) }
    ) {
        markerPositions.forEach { (coord, title) ->
            Marker(
                state = MarkerState(GmsLatLng(coord.latitude, coord.longitude)),
                title = title
            )
        }
    }
}
