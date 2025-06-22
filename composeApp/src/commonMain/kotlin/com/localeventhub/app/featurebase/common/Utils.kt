package com.localeventhub.app.featurebase.common

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.math.absoluteValue

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

}



