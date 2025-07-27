package com.localeventhub.app.featurebase.presentation.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import dev.jordond.compass.Location
import dev.jordond.compass.Priority
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.LocationRequest
import dev.jordond.compass.geolocation.TrackingStatus
import dev.jordond.compass.geolocation.currentLocationOrNull
import dev.jordond.compass.geolocation.isPermissionDenied
import dev.jordond.compass.geolocation.isPermissionDeniedForever
import dev.jordond.compass.geolocation.mobile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun LocationTracking(onLocationChanged : (Location) -> Unit){
    println("Tracking Location")
    val geolocator: Geolocator = Geolocator.mobile()
    val coroutineScope = rememberCoroutineScope()
    coroutineScope.launch {
        val location: Location? = geolocator.currentLocationOrNull()
        println("Tracking Location - $location")
        if (location != null) {
            onLocationChanged(location)
        }
    }
// Handle the result:



    /*val geoLocator = remember { Geolocator.mobile() }
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
                        geoLocator.startTracking(LocationRequest(priority = Priority.LowPower))
                    }
                    is TrackingStatus.Tracking -> {}
                    is TrackingStatus.Update -> {}
                    is TrackingStatus.Error -> {
                        val error: GeolocatorResult.Error = status.cause
                        println("TRACKING ERROR: $error")
                        val permissionDeniedForever = error.isPermissionDenied()
                        println("TRACKING PERMISSION DENIED: $permissionDeniedForever")
                    }
                }
            }
    }*/


    /*DisposableEffect(Unit) {
        onDispose {
            geoLocator.stopTracking()
        }
    }*/


}