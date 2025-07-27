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
import cocoapods.GoogleMaps.GMSMapViewDelegateProtocol
import cocoapods.GoogleMaps.GMSMapViewOptions
import cocoapods.GoogleMaps.GMSMarker
/*import cocoapods.GoogleMaps.GMSCameraPosition
import cocoapods.GoogleMaps.GMSCameraUpdate
import cocoapods.GoogleMaps.GMSMapView
import cocoapods.GoogleMaps.GMSMapViewDelegateProtocol
import cocoapods.GoogleMaps.GMSMapViewOptions
import cocoapods.GoogleMaps.GMSMarker*/
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.cValue
import platform.CoreLocation.CLLocationCoordinate2D
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun GoogleMap(
    modifier: Modifier,
    currentLocationPosition: LatLng,
    markerPosition: LatLng,
    title: String,
    onMapClick: (LatLng) -> Unit
) {
    val mapView = remember { GMSMapView() }
    val cameraPosition = GMSCameraPosition.cameraWithLatitude(
        latitude = markerPosition.latitude,
        longitude = markerPosition.longitude,
        zoom = 10.0f
    )
    val cameraUpdate = GMSCameraUpdate.setCamera(cameraPosition)

    // Create and configure the marker
    LaunchedEffect(markerPosition, title) {
        val marker = GMSMarker().apply {
            position = cameraPosition.target
            this.title = title
            map = mapView // Attach the marker to the map
        }
    }

    mapView.moveCamera(cameraUpdate)

    UIKitView(
        modifier = modifier.fillMaxSize(),
        factory = { mapView },
        update = { view ->
            // Optional: Handle updates to the map view if needed
        }
    )

}