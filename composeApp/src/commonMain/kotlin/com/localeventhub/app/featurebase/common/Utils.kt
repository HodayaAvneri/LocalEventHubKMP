package com.localeventhub.app.featurebase.common

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.math.absoluteValue
import kotlin.math.*

class Utils {

    fun formatDouble(value: Double): String {
        val stringValue = value.toString()
        val dotIndex = stringValue.indexOf('.')

        return if (dotIndex != -1) {
            val decimalPart = stringValue.substring(dotIndex + 1)
            if (decimalPart.length > 2) {
                stringValue.substring(0, dotIndex + 3) // Keep only 2 decimal places
            } else {
                stringValue // If already 2 or fewer decimal places, return as is
            }
        } else {
            stringValue // If no decimal point, return as is
        }
    }

    fun daysUntilTargetDate(epochMillis: Long): String {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val targetDate = Instant.fromEpochMilliseconds(epochMillis).toLocalDateTime(TimeZone.currentSystemDefault()).date

        val daysDiff = targetDate.daysUntil(now)

        return when {
            daysDiff == 0 -> "Today"
            daysDiff == 1 -> "1 day ago"
            daysDiff > 1 -> "$daysDiff days ago"
            daysDiff == -1 -> "in 1 day"
            daysDiff < 0 -> "in ${-daysDiff} days"
            else -> "Unknown"
        }
    }

    private fun Double.toRadians(): Double = this * (PI / 180)

    fun haversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371000.0 // meters
        val dLat = (lat2 - lat1).toRadians()
        val dLon = (lon2 - lon1).toRadians()
        val a = sin(dLat / 2).pow(2) +
                cos(lat1.toRadians()) * cos(lat2.toRadians()) *
                sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }

}



