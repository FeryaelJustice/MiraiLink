package com.feryaeljustice.mirailink.ui.screens.photo

import android.net.Uri
import com.feryaeljustice.mirailink.domain.error.UnknownError
import com.feryaeljustice.mirailink.domain.usecase.photos.UploadUserPhotoUseCase
import com.feryaeljustice.mirailink.ui.error.toUiError
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
    val koinTestRule =
        KoinTestRule.create {
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

            assert(viewModel.uploadSucceeded.value)
            assert(viewModel.error.value == null)
        }

    @Test
    fun `upload image error`() =
        runTest {
            val uri = mockk<Uri>()
            coEvery { uploadUserPhotoUseCase.invoke(uri) } returns
                MiraiLinkResult.Error(UnknownError)

            viewModel.uploadImage(uri)
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            assert(!viewModel.uploadSucceeded.value)
            assert(viewModel.error.value == UnknownError.toUiError())
        }
}
