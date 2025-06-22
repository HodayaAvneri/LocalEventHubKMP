package com.localeventhub.app.dashboard.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class EventLocation(
    val latitude: Double,                   // Latitude of the event
    val longitude: Double,                  // Longitude of the event
    val address: String?
)