package com.localeventhub.app.expect

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun GoogleMap(
    modifier: Modifier = Modifier,
    currentLocationPosition: LatLng,
    markerPosition: LatLng,
    onMapClick: (LatLng) -> Unit = {}
)

data class LatLng(val latitude: Double, val longitude: Double)