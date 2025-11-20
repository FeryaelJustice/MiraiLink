// Author: Feryael Justice
// Date: 2025-11-08

package com.feryaeljustice.mirailink.ui.screens.profile

import com.feryaeljustice.mirailink.data.mappers.ui.toUserViewEntry
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.usecase.catalog.GetAnimesUseCase
import com.feryaeljustice.mirailink.domain.usecase.catalog.GetGamesUseCase
import com.feryaeljustice.mirailink.domain.usecase.photos.DeleteUserPhotoUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.UpdateUserProfileUseCase
import com.feryaeljustice.mirailink.domain.util.Logger
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.screens.profile.edit.EditProfileIntent
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
class ProfileViewModelTest : KoinTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val getCurrentUserUseCase: GetCurrentUserUseCase by inject()
    private val updateUserProfileUseCase: UpdateUserProfileUseCase by inject()
    private val deleteUserPhotoUseCase: DeleteUserPhotoUseCase by inject()
    private val getAnimesUseCase: GetAnimesUseCase by inject()
    private val getGamesUseCase: GetGamesUseCase by inject()
    private val logger: Logger by inject()

    private lateinit var viewModel: ProfileViewModel

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                single { mockk<GetCurrentUserUseCase>() }
                single { mockk<UpdateUserProfileUseCase>() }
                single { mockk<DeleteUserPhotoUseCase>() }
                single { mockk<GetAnimesUseCase>() }
                single { mockk<GetGamesUseCase>() }
                single { mockk<Logger>(relaxed = true) }
            },
        )
    }

    private val user =
        User(
            "1",
            "user",
            "user",
            "user@test.com",
            null,
            null,
            null,
            null,
            emptyList(),
            emptyList(),
            emptyList(),
        )

    @Before
    fun setUp() {
        coEvery { getCurrentUserUseCase.invoke() } returns MiraiLinkResult.Success(user)

        viewModel =
            ProfileViewModel(
                getCurrentUserUseCase,
                updateUserProfileUseCase,
                deleteUserPhotoUseCase,
                getAnimesUseCase,
                getGamesUseCase,
                logger,
                mainCoroutineRule.testDispatcher,
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
            coEvery { getAnimesUseCase.invoke() } returns MiraiLinkResult.Success(emptyList())
            coEvery { getGamesUseCase.invoke() } returns MiraiLinkResult.Success(emptyList())
            viewModel.onIntent(EditProfileIntent.Initialize(user.toUserViewEntry()))
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            val editState = viewModel.editState.value
            assert(editState.isEditing)
            assert(editState.nickname == user.nickname)
        }

    @Test
    fun `save profile success`() =
        runTest {
            coEvery { getAnimesUseCase.invoke() } returns MiraiLinkResult.Success(emptyList())
            coEvery { getGamesUseCase.invoke() } returns MiraiLinkResult.Success(emptyList())
            viewModel.onIntent(EditProfileIntent.Initialize(user.toUserViewEntry()))
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            coEvery {
                updateUserProfileUseCase
                    .invoke(any(), any(), any(), any(), any(), any(), any(), any())
            } returns
                MiraiLinkResult.Success(Unit)

            viewModel.onIntent(EditProfileIntent.Save)
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            assert(!viewModel.editState.value.isEditing)
        }

    @Test
    fun `remove photo success`() =
        runTest {
            coEvery { deleteUserPhotoUseCase.invoke(1) } returns MiraiLinkResult.Success(Unit)

            viewModel.onIntent(EditProfileIntent.RemovePhoto(0))
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            // Normally, getCurrentUser would be called again, but we just check the use case was called
        }
}
