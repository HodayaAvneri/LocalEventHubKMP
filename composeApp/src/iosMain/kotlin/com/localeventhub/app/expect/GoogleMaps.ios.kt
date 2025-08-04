package com.localeventhub.app.expect

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import cocoapods.GoogleMaps.GMSCameraPosition
import cocoapods.GoogleMaps.GMSCameraUpdate
import cocoapods.GoogleMaps.GMSMapView
import cocoapods.GoogleMaps.GMSMarker
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectMake
import platform.CoreLocation.CLLocationCoordinate2DMake

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun GoogleMap(
    modifier: Modifier,
    currentLocationPosition: LatLng,
    markerPositions: List<Pair<LatLng, String>>,
    onMapClick: (LatLng) -> Unit
) {
    val firstTarget = markerPositions.firstOrNull()?.first ?: currentLocationPosition

    val mapView = remember {
        val camera = GMSCameraPosition.cameraWithLatitude(
            latitude = firstTarget.latitude,
            longitude = firstTarget.longitude,
            zoom = 14f
        )
        GMSMapView(frame = CGRectMake(0.0, 0.0, 0.0, 0.0), camera = camera)
    }

    LaunchedEffect(markerPositions) {
        mapView.clear()
        markerPositions.forEach { (coord, title) ->
            GMSMarker().apply {
                position = CLLocationCoordinate2DMake(coord.latitude, coord.longitude)
                this.title = title
                map = mapView
            }
        }

        val moveTo = markerPositions.firstOrNull()?.first ?: currentLocationPosition
        val update = GMSCameraUpdate.setCamera(
            GMSCameraPosition.cameraWithLatitude(
                latitude = moveTo.latitude,
                longitude = moveTo.longitude,
                zoom = 14f
            )
        )
        mapView.moveCamera(update)
    }

    UIKitView(
        modifier = modifier.fillMaxSize(),
        factory = { mapView },
        update = { _ -> }
    )
}
