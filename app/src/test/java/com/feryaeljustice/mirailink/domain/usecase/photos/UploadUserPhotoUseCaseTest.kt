package com.feryaeljustice.mirailink.domain.usecase.photos

import android.net.Uri
import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author Feryael Justice
 * @since 26/10/2024
 */
@ExperimentalCoroutinesApi
class UploadUserPhotoUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repo: UserRepository

    private lateinit var uploadUserPhotoUseCase: UploadUserPhotoUseCase

    @Before
    fun onBefore() {
        uploadUserPhotoUseCase = UploadUserPhotoUseCase(repo)
    }

    @Test
    fun `when repository uploads photo successfully, return success with photo url`() = runTest {
        // Given
        val photoUri = mockk<Uri>()
        val photoUrl = "http://example.com/photo.jpg"
        coEvery { repo.uploadUserPhoto(photoUri) } returns MiraiLinkResult.Success(photoUrl)

        // When
        val result = uploadUserPhotoUseCase(photoUri)

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(photoUrl, (result as MiraiLinkResult.Success).data)
    }

    @Test
    fun `when repository fails to upload photo, return error`() = runTest {
        // Given
        val photoUri = mockk<Uri>()
        val errorResult = MiraiLinkResult.Error("Could not upload photo")
        coEvery { repo.uploadUserPhoto(photoUri) } returns errorResult

        // When
        val result = uploadUserPhotoUseCase(photoUri)

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.message, (result as MiraiLinkResult.Error).message)
    }

    @Test
    fun `when repository throws an exception, use case should return error`() = runTest {
        // Given
        val photoUri = mockk<Uri>()
        val exception = RuntimeException("Network error")
        coEvery { repo.uploadUserPhoto(photoUri) } throws exception

        // When
        val result = uploadUserPhotoUseCase(photoUri)

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals("UploadUserPhotoUseCase error", (result as MiraiLinkResult.Error).message)
        assertEquals(exception, result.exception)
    }
}