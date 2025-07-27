package com.localeventhub.app.dashboard.data.model

import kotlinx.serialization.Serializable

@Serializable
data class EventLocationDto(
    val latitude: Double,                   // Latitude of the event
    val longitude: Double,                  // Longitude of the event
    val address: String?
)