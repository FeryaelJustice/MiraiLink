/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.data.mappers

import com.feryaeljustice.mirailink.data.model.UserPhotoDto
import org.junit.Assert.assertEquals
import org.junit.Test

class MediaMapperTest {

    @Test
    fun `UserPhotoDto maps to UserPhoto domain model correctly`() {
        // Given
        val userPhotoDto = UserPhotoDto(
            id = "photo1",
            userId = "user1",
            url = "http://example.com/photo.jpg",
            position = 1
        )

        // When
        val userPhoto = userPhotoDto.toDomain()

        // Then
        assertEquals(userPhotoDto.userId, userPhoto.userId)
        assertEquals(userPhotoDto.url, userPhoto.url)
        assertEquals(userPhotoDto.position, userPhoto.position)
    }
}
