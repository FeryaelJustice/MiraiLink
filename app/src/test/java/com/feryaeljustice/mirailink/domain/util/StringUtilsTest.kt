package com.feryaeljustice.mirailink.domain.util

import com.feryaeljustice.mirailink.domain.constants.TEMPORAL_PLACEHOLDER_PICTURE_URL
import com.google.common.truth.Truth.assertThat
import org.junit.Test

/** Boundary tests for validation and formatting string extensions. */
class StringUtilsTest {
    /** Covers valid, blank and injection-like URLs. */
    @Test
    fun `url validation accepts safe http urls only`() {
        assertThat("https://example.com/photo.jpg".isValidUrl()).isTrue()
        assertThat(" ".isValidUrl()).isFalse()
        assertThat("https://example.com/photo.jpg;drop table users".isValidUrl()).isFalse()
    }

    /** Verifies nullable and invalid media URLs use the stable placeholder. */
    @Test
    fun `formatted url returns placeholder for absent or invalid input`() {
        assertThat(null.getFormattedUrl()).isEqualTo(TEMPORAL_PLACEHOLDER_PICTURE_URL)
        assertThat("not a url".getFormattedUrl()).isEqualTo(TEMPORAL_PLACEHOLDER_PICTURE_URL)
        assertThat("https://example.com/image.png".getFormattedUrl())
            .isEqualTo("https://example.com/image.png")
    }

    /** Covers accepted and rejected email formats. */
    @Test
    fun `email validation checks format and unsafe content`() {
        assertThat("person@example.com".isEmailValid()).isTrue()
        assertThat("person+tag@example.com".isEmailValid()).isFalse()
        assertThat("person@example.com;drop table users".isEmailValid()).isFalse()
    }

    /** Documents the current minimum password contract and SQL-input guard. */
    @Test
    fun `password validation enforces minimum length and safe input`() {
        assertThat("abc".isPasswordValid()).isFalse()
        assertThat("abcd".isPasswordValid()).isTrue()
        assertThat("abcd;".isPasswordValid()).isFalse()
    }

    /** Verifies capitalization and nullable convenience helpers at their boundaries. */
    @Test
    fun `string convenience helpers preserve expected values`() {
        assertThat("mirai".superCapitalize()).isEqualTo("Mirai")
        assertThat("".superCapitalize()).isEmpty()
        assertThat(null.isStringNotEmpty()).isFalse()
        assertThat("".isStringNotEmpty()).isFalse()
        assertThat("value".isStringNotEmpty()).isTrue()
    }

    /** Covers representative SQL injection markers without coupling to every keyword. */
    @Test
    fun `safe sql input rejects suspicious statements case insensitively`() {
        assertThat("normal profile text".isSafeSqlInput()).isTrue()
        assertThat("Robert'); DROP TABLE users;".isSafeSqlInput()).isFalse()
        assertThat("value OR 1=1".isSafeSqlInput()).isFalse()
    }
}
