// Author: Feryael Justice
// Date: 2025-11-08

package com.feryaeljustice.mirailink.data.mappers

import com.feryaeljustice.mirailink.data.model.UserPhotoDto
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.test.KoinTest

class MediaMapperTest : KoinTest {

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
