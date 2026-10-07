package com.example.kmpsamples

import kotlin.random.Random
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

fun randomSampleId(): String = Random.nextLong().toString()

/** Multiplatform-safe two-decimal formatting (avoids JVM-only String.format). */
fun formatFixed2(value: Float): String {
    val hundredths = ((value * 100f) + if (value >= 0f) 0.5f else -0.5f).toInt()
    val whole = hundredths / 100
    val fraction = kotlin.math.abs(hundredths % 100)
    return "$whole.${fraction.toString().padStart(2, '0')}"
}

fun nowLocalDateTime(): LocalDateTime =
    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

fun LocalDateTime.plusHours(hoursToAdd: Int): LocalDateTime =
    toInstant(TimeZone.currentSystemDefault())
        .plus(hoursToAdd.hours)
        .toLocalDateTime(TimeZone.currentSystemDefault())

fun LocalDateTime.plusDays(daysToAdd: Int): LocalDateTime =
    toInstant(TimeZone.currentSystemDefault())
        .plus(daysToAdd.days)
        .toLocalDateTime(TimeZone.currentSystemDefault())

fun formatReminder(dateTime: LocalDateTime): String =
    formatDateTime(dateTime, includeYear = true)

fun formatTaskDue(dateTime: LocalDateTime): String =
    formatDateTime(dateTime, includeYear = false)

private fun formatDateTime(dateTime: LocalDateTime, includeYear: Boolean): String {
    val month = dateTime.month.name.lowercase().replaceFirstChar { it.titlecase() }.take(3)
    val hour = dateTime.hour
    val minute = dateTime.minute.toString().padStart(2, '0')
    val amPm = if (hour >= 12) "PM" else "AM"
    val hour12 = when (val h = hour % 12) {
        0 -> 12
        else -> h
    }
    val dayPart = if (includeYear) {
        "$month ${dateTime.dayOfMonth}, ${dateTime.year}"
    } else {
        "$month ${dateTime.dayOfMonth}"
    }
    return "$dayPart - $hour12:$minute $amPm"
}
