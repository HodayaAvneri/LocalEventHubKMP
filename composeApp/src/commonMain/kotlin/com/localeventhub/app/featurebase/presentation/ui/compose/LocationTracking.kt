package com.localeventhub.app.featurebase.presentation.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import dev.jordond.compass.Location
import dev.jordond.compass.Priority
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.LocationRequest
import dev.jordond.compass.geolocation.TrackingStatus
import dev.jordond.compass.geolocation.isPermissionDeniedForever
import dev.jordond.compass.geolocation.mobile
import kotlinx.coroutines.flow.first

@Composable
fun LocationTracking(onLocationChanged : (Location) -> Unit){
    val geoLocator = remember { Geolocator.mobile() }
    LaunchedEffect(Unit){
        geoLocator.locationUpdates.first {
            onLocationChanged(it)
            true
        }
    }
    LaunchedEffect(Unit){
        geoLocator.trackingStatus.collect { status ->
                when (status) {
                    is TrackingStatus.Idle -> {
                        geoLocator.startTracking(LocationRequest(priority = Priority.HighAccuracy))
                    }
                    is TrackingStatus.Tracking -> {}
                    is TrackingStatus.Update -> {}
                    is TrackingStatus.Error -> {
                        val error: GeolocatorResult.Error = status.cause
                        println("TRACKING ERROR: $error")
                        val permissionDeniedForever = error.isPermissionDeniedForever()
                        println("TRACKING PERMISSION DENIED: $permissionDeniedForever")
                    }
                }
            }
    }


    DisposableEffect(Unit) {
        onDispose {
            geoLocator.stopTracking()
        }
    }


}