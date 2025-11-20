// Author: Feryael Justice
// Date: 2025-11-08

package com.feryaeljustice.mirailink.ui.screens.photo

import android.net.Uri
import com.feryaeljustice.mirailink.domain.usecase.photos.UploadUserPhotoUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject

@ExperimentalCoroutinesApi
class ProfilePictureViewModelTest : KoinTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val uploadUserPhotoUseCase: UploadUserPhotoUseCase by inject()

    private lateinit var viewModel: ProfilePictureViewModel

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                single { mockk<UploadUserPhotoUseCase>() }
            },
        )
    }

    @Before
    fun setUp() {
        viewModel =
            ProfilePictureViewModel(uploadUserPhotoUseCase, mainCoroutineRule.testDispatcher)
    }

    @Test
    fun `upload image success`() =
        runTest {
            val uri = mockk<Uri>()
            val imageUrl = "http://example.com/image.jpg"

            coEvery { uploadUserPhotoUseCase.invoke(uri) } returns
                MiraiLinkResult.Success(
                    imageUrl,
                )

            viewModel.uploadImage(uri)
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            val result = viewModel.uploadResult.value
            assert(result is MiraiLinkResult.Success)
            assert((result as MiraiLinkResult.Success).data == imageUrl)
        }

    @Test
    fun `upload image error`() =
        runTest {
            val uri = mockk<Uri>()
            val errorMessage = "Upload failed"

            coEvery { uploadUserPhotoUseCase.invoke(uri) } returns
                MiraiLinkResult.Error(
                    errorMessage,
                )

            viewModel.uploadImage(uri)
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            val result = viewModel.uploadResult.value
            assert(result is MiraiLinkResult.Error)
            assert((result as MiraiLinkResult.Error).message == errorMessage)
        }
}
