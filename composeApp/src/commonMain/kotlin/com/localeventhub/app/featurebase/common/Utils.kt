package com.localeventhub.app.featurebase.common

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
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

    fun daysUntilTargetDate(targetDate: String): String {
        // Parse the target date (assuming format MM/dd/yyyy)
        val parts = targetDate.split("/")
        val target = LocalDate(parts[2].toInt(), parts[1].toInt(), parts[0].toInt())

        // Get current date in current timezone
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

        // Calculate days difference
        val daysDifference = today.daysUntil(target)

        return when {
            daysDifference > 0 -> "ends in $daysDifference days"
            daysDifference < 0 -> "ended ${daysDifference.absoluteValue} days ago"
            else -> "ends today"
        }
    }

}



