package com.localeventhub.app.expect

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
actual fun GoogleMap(
    modifier: Modifier,
    currentLocationPosition: LatLng,
    markerPosition: LatLng,
    onMapClick: (LatLng) -> Unit
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            com.google.android.gms.maps.model.LatLng(
                markerPosition.latitude,
                markerPosition.longitude
            ),
            10f
        )
    }

    com.google.maps.android.compose.GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = { latLng ->
            onMapClick(LatLng(latLng.latitude, latLng.longitude))
        }
    ) {
        // Add markers or other map elements if needed
        Marker(
            state = MarkerState(
                position = com.google.android.gms.maps.model.LatLng(
                    markerPosition.latitude,
                    markerPosition.longitude
                )
            ),
            title = "Marker",
            snippet = "Sample marker"
        )
    }
}