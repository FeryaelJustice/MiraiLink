package com.feryaeljustice.mirailink.domain.util

import android.util.Log
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.FormatStyle
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun parseDate(dateString: String): Date =
    try {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        formatter.parse(dateString)
    } catch (e: ParseException) {
        Log.e("DateParsing", "Error parsing date: $dateString -> ${e.message}")
        Date()
    }

fun formatTimestamp(timestamp: Long): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return Instant
        .ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}

fun formatDateSeparator(timestamp: Long): String {
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
    return Instant
        .ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}

object DateSerializer : KSerializer<Date> {
    // It's good practice to use a specific, consistent format, like ISO 8601.
    // Also, explicitly use UTC to avoid timezone issues.
    private val dateFormat =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: Date,
    ) {
        encoder.encodeString(dateFormat.format(value))
    }

    override fun deserialize(decoder: Decoder): Date =
        dateFormat.parse(decoder.decodeString())
            ?: throw IllegalArgumentException("Invalid date format")
}

/** De millis del DatePicker a "yyyy-MM-dd" en tu zona */
fun millisToBackendDate(millis: Long): String =
    Instant
        .ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .toString() // "yyyy-MM-dd"

/** Normaliza cualquier String (con o sin 'Z') a "yyyy-MM-dd" */
fun toBackendDate(input: String): String =
    try {
        val safeInput = input.takeIf { it.isNotBlank() }
        when {
            safeInput == null -> {
                LocalDate.now().toString()
            }

            // blank o null → hoy
            else -> {
                try {
                    // Caso ISO con hora: "1999-06-26T22:00:00.000Z"
                    Instant
                        .parse(safeInput)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .toString()
                } catch (_: Exception) {
                    // Caso ya "yyyy-MM-dd" (o parseable como LocalDate)
                    LocalDate.parse(safeInput).toString()
                }
            }
        }
    } catch (_: Exception) {
        LocalDate.now().toString() // fallback absoluto
    }

/** Para inicializar el DatePicker desde "yyyy-MM-dd" */
fun backendDateToMillis(date: String): Long =
    LocalDate
        .parse(date)
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

fun String?.toAgeOrNull(): String? {
    if (this.isNullOrBlank()) return null
    return try {
        val birthDate = LocalDate.parse(this) // "YYYY-MM-DD"
        val today = LocalDate.now()
        val age = Period.between(birthDate, today).years
        return age.toString()
    } catch (_: DateTimeParseException) {
        null
    }
}
