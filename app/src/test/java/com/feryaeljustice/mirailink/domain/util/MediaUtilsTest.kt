package com.feryaeljustice.mirailink.domain.util

import com.feryaeljustice.mirailink.domain.model.user.UserPhoto
import com.google.common.truth.Truth.assertThat
import org.junit.Test

/** Pure URL resolution and ordering tests for media helpers. */
class MediaUtilsTest {
    /** Verifies relative URL resolution for every slash-boundary combination. */
    @Test
    fun `resolve photo url joins base and relative paths once`() {
        assertThat(resolvePhotoUrl("https://api.example.com", "images/a.jpg"))
            .isEqualTo("https://api.example.com/images/a.jpg")
        assertThat(resolvePhotoUrl("https://api.example.com/", "/images/a.jpg"))
            .isEqualTo("https://api.example.com/images/a.jpg")
        assertThat(resolvePhotoUrl("https://api.example.com/", "images/a.jpg"))
            .isEqualTo("https://api.example.com/images/a.jpg")
        assertThat(resolvePhotoUrl("https://api.example.com", "/images/a.jpg"))
            .isEqualTo("https://api.example.com/images/a.jpg")
    }

    /** Verifies absolute URLs are preserved without being prefixed. */
    @Test
    fun `resolve photo url preserves absolute url`() {
        assertThat(resolvePhotoUrl("https://api.example.com", "https://cdn.example.com/a.jpg"))
            .isEqualTo("https://cdn.example.com/a.jpg")
    }

    /** Verifies photo collections are sorted and resolved without mutating other fields. */
    @Test
    fun `resolve photo urls sorts by position and resolves relative entries`() {
        // Given
        val photos =
            listOf(
                UserPhoto(userId = "1", url = "https://cdn.example.com/two.jpg", position = 2),
                UserPhoto(userId = "1", url = "/one.jpg", position = 1),
            )

        // When
        val result = resolvePhotoUrls("https://api.example.com", photos)

        // Then
        assertThat(result.map { it.position }).containsExactly(1, 2).inOrder()
        assertThat(result[0].url).isEqualTo("https://api.example.com/one.jpg")
        assertThat(result[1].url).isEqualTo("https://cdn.example.com/two.jpg")
        assertThat(result.all { it.userId == "1" }).isTrue()
    }
}
