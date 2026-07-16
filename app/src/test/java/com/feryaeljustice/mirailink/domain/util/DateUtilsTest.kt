package com.feryaeljustice.mirailink.domain.util

import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/** Deterministic parsing, formatting, serialization and age tests. */
class DateUtilsTest {
    private lateinit var originalTimeZone: TimeZone
    private lateinit var originalLocale: Locale

    /** Fixes locale and time zone so date assertions are deterministic on every machine. */
    @Before
    fun setUp() {
        originalTimeZone = TimeZone.getDefault()
        originalLocale = Locale.getDefault()
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        Locale.setDefault(Locale.US)
    }

    /** Restores process-wide values changed by the test fixture. */
    @After
    fun tearDown() {
        TimeZone.setDefault(originalTimeZone)
        Locale.setDefault(originalLocale)
    }

    /** Verifies the backend ISO instant parser uses UTC. */
    @Test
    fun `parse date converts iso timestamp to expected epoch`() {
        assertThat(parseDate("1970-01-01T00:00:00.000Z").time).isEqualTo(0L)
    }

    /** Verifies timestamp and date-picker conversions under a fixed time zone. */
    @Test
    fun `timestamp helpers use the configured local zone`() {
        val millis = LocalDate.of(2025, 1, 2).atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli()

        assertThat(formatTimestamp(millis)).isEqualTo("00:00")
        assertThat(millisToBackendDate(millis)).isEqualTo("2025-01-02")
        assertThat(backendDateToMillis("2025-01-02")).isEqualTo(millis)
    }

    /** Verifies plain dates and ISO instants normalize to the backend date format. */
    @Test
    fun `backend date normalization supports date and instant inputs`() {
        assertThat(toBackendDate("2025-01-02")).isEqualTo("2025-01-02")
        assertThat(toBackendDate("2025-01-02T23:45:00.000Z")).isEqualTo("2025-01-02")
    }

    /** Verifies age parsing and invalid/null boundaries. */
    @Test
    fun `age conversion returns years or null for invalid values`() {
        val twentyYearsAgo = LocalDate.now().minusYears(20).toString()

        assertThat(twentyYearsAgo.toAgeOrNull()).isEqualTo("20")
        assertThat("not-a-date".toAgeOrNull()).isNull()
        assertThat(null.toAgeOrNull()).isNull()
        assertThat("".toAgeOrNull()).isNull()
    }

    /** Verifies the custom serializer writes and restores the exact instant. */
    @Test
    fun `date serializer round trips iso utc value`() {
        val value = Date(1_735_776_000_000L)

        val encoded = Json.encodeToString(DateSerializer, value)
        val decoded = Json.decodeFromString(DateSerializer, encoded)

        assertThat(decoded).isEqualTo(value)
        assertThat(encoded).isEqualTo("\"2025-01-02T00:00:00.000Z\"")
    }
}
