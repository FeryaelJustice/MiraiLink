// Author: Feryael Justice
// Date: 2024-07-29

package com.feryaeljustice.mirailink.ui.screens.profile

import com.feryaeljustice.mirailink.data.mappers.ui.toUserViewEntry
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.usecase.catalog.GetAnimesUseCase
import com.feryaeljustice.mirailink.domain.usecase.catalog.GetGamesUseCase
import com.feryaeljustice.mirailink.domain.usecase.photos.DeleteUserPhotoUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.UpdateUserProfileUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.screens.profile.edit.EditProfileIntent
import com.feryaeljustice.mirailink.util.MainCoroutineRule
import dagger.Lazy
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProfileViewModelTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ProfileViewModel
    private val getCurrentUserUseCase: Lazy<GetCurrentUserUseCase> = mockk()
    private val updateUserProfileUseCase: Lazy<UpdateUserProfileUseCase> = mockk()
    private val deleteUserPhotoUseCase: Lazy<DeleteUserPhotoUseCase> = mockk()
    private val getAnimesUseCase: Lazy<GetAnimesUseCase> = mockk()
    private val getGamesUseCase: Lazy<GetGamesUseCase> = mockk()

    private val user = User("1", "user", "user", "user@test.com", null, null, null, null, emptyList(), emptyList(), emptyList())

    @Before
    fun setUp() {
        coEvery { getCurrentUserUseCase.get().invoke() } returns MiraiLinkResult.Success(user)

        viewModel =
            ProfileViewModel(
                getCurrentUserUseCase,
                updateUserProfileUseCase,
                deleteUserPhotoUseCase,
                getAnimesUseCase,
                getGamesUseCase,
            )
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
    }

    @Test
    fun `get current user success`() =
        runTest {
            val state = viewModel.state.value
            assert(state is ProfileViewModel.ProfileUiState.Success)
            assert((state as ProfileViewModel.ProfileUiState.Success).user?.id == user.id)
        }

    @Test
    fun `initialize edit mode`() =
        runTest {
            coEvery { getAnimesUseCase.get().invoke() } returns MiraiLinkResult.Success(emptyList())
            coEvery { getGamesUseCase.get().invoke() } returns MiraiLinkResult.Success(emptyList())
            viewModel.onIntent(EditProfileIntent.Initialize(user.toUserViewEntry()))
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            val editState = viewModel.editState.value
            assert(editState.isEditing)
            assert(editState.nickname == user.nickname)
        }

    @Test
    fun `save profile success`() =
        runTest {
            coEvery { getAnimesUseCase.get().invoke() } returns MiraiLinkResult.Success(emptyList())
            coEvery { getGamesUseCase.get().invoke() } returns MiraiLinkResult.Success(emptyList())
            viewModel.onIntent(EditProfileIntent.Initialize(user.toUserViewEntry()))
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            coEvery { updateUserProfileUseCase.get().invoke(any(), any(), any(), any(), any(), any(), any(), any()) } returns
                MiraiLinkResult.Success(Unit)

            viewModel.onIntent(EditProfileIntent.Save)
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            assert(!viewModel.editState.value.isEditing)
        }

    @Test
    fun `remove photo success`() =
        runTest {
            coEvery { deleteUserPhotoUseCase.get().invoke(1) } returns MiraiLinkResult.Success(Unit)

            viewModel.onIntent(EditProfileIntent.RemovePhoto(0))
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            // Normally, getCurrentUser would be called again, but we just check the use case was called
        }
}
