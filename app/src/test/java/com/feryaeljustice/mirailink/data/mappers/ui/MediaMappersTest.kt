/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.data.mappers.ui

import com.feryaeljustice.mirailink.domain.model.user.UserPhoto
import com.feryaeljustice.mirailink.ui.viewentries.media.UserPhotoViewEntry
import org.junit.Assert.assertEquals
import org.junit.Test

class MediaMappersTest {

    @Test
    fun `UserPhoto to UserPhotoViewEntry mapping is correct`() {
        // Given
        val userPhoto = UserPhoto(
            userId = "user1",
            url = "http://example.com/photo.jpg",
            position = 1
        )

        // When
        val viewEntry = userPhoto.toUserPhotoViewEntry()

        // Then
        assertEquals(userPhoto.userId, viewEntry.userId)
        assertEquals(userPhoto.url, viewEntry.url)
        assertEquals(userPhoto.position, viewEntry.position)
    }

    @Test
    fun `UserPhotoViewEntry to PhotoSlotViewEntry mapping is correct`() {
        // Given
        val userPhotoViewEntry = UserPhotoViewEntry(
            userId = "user1",
            url = "http://example.com/photo.jpg",
            position = 1
        )

        // When
        val photoSlotViewEntry = userPhotoViewEntry.toPhotoSlotViewEntry()

        // Then
        assertEquals(userPhotoViewEntry.url, photoSlotViewEntry.url)
        assertEquals(userPhotoViewEntry.position, photoSlotViewEntry.position)
    }
}
