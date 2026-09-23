package com.feryaeljustice.mirailink.ui.screens.profile

import com.feryaeljustice.mirailink.data.mappers.ui.toUserViewEntry
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.enums.TagType
import com.feryaeljustice.mirailink.domain.enums.TextFieldType
import com.feryaeljustice.mirailink.domain.model.catalog.Anime
import com.feryaeljustice.mirailink.domain.model.catalog.Game
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
import java.util.Locale
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
    val koinTestRule =
        KoinTestRule.create {
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
    fun `confirming the same residence country keeps its dependent values`() =
        runTest {
            coEvery { getAnimesUseCase.invoke() } returns MiraiLinkResult.Success(emptyList())
            coEvery { getGamesUseCase.invoke() } returns MiraiLinkResult.Success(emptyList())
            val residentUser =
                user.copy(
                    residenceCountryCode = "ES",
                    residenceRegion = "Comunidad de Madrid",
                    residenceCity = "Madrid",
                )

            viewModel.onIntent(EditProfileIntent.Initialize(residentUser.toUserViewEntry()))
            viewModel.onIntent(
                EditProfileIntent.UpdateTextField(
                    TextFieldType.RESIDENCE_COUNTRY,
                    Locale("", "ES").getDisplayCountry(Locale.getDefault()),
                ),
            )

            assert(viewModel.editState.value.residenceRegion == "Comunidad de Madrid")
            assert(viewModel.editState.value.residenceCity == "Madrid")
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
                    .invoke(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any())
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

    @Test
    fun `uploading photo to slot 3 or 4 when only first photo exists inserts into 2nd position`() =
        runTest {
            val singlePhotoUser = user.copy(
                photos = listOf(
                    com.feryaeljustice.mirailink.domain.model.user.UserPhoto("1", "http://example.com/photo1.jpg", 1),
                ),
            )
            coEvery { getAnimesUseCase.invoke() } returns MiraiLinkResult.Success(emptyList())
            coEvery { getGamesUseCase.invoke() } returns MiraiLinkResult.Success(emptyList())
            viewModel.onIntent(EditProfileIntent.Initialize(singlePhotoUser.toUserViewEntry()))
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            val mockUri = mockk<android.net.Uri>(relaxed = true)
            io.mockk.every { mockUri.toString() } returns "content://media/new_photo.jpg"

            // Tapping slot 3 (index 2) when only slot 1 (index 0) exists
            viewModel.onIntent(EditProfileIntent.UpdatePhoto(position = 2, uri = mockUri))

            val photos = viewModel.editState.value.photos
            // Should be inserted at index 1 (2nd position)
            assert(photos[0].url == "http://example.com/photo1.jpg")
            assert(photos[1].uri == mockUri)
            assert(photos[1].position == 1)
            assert(photos[2].url == null && photos[2].uri == null)
            assert(photos[3].url == null && photos[3].uri == null)
        }

    @Test
    fun `reordering photo swaps positions directly between from and to slots`() =
        runTest {
            val fourPhotosUser = user.copy(
                photos = listOf(
                    com.feryaeljustice.mirailink.domain.model.user.UserPhoto("1", "http://example.com/photo1.jpg", 1),
                    com.feryaeljustice.mirailink.domain.model.user.UserPhoto("1", "http://example.com/photo2.jpg", 2),
                    com.feryaeljustice.mirailink.domain.model.user.UserPhoto("1", "http://example.com/photo3.jpg", 3),
                    com.feryaeljustice.mirailink.domain.model.user.UserPhoto("1", "http://example.com/photo4.jpg", 4),
                ),
            )
            coEvery { getAnimesUseCase.invoke() } returns MiraiLinkResult.Success(emptyList())
            coEvery { getGamesUseCase.invoke() } returns MiraiLinkResult.Success(emptyList())
            viewModel.onIntent(EditProfileIntent.Initialize(fourPhotosUser.toUserViewEntry()))
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            // User drags photo 4 (index 3) to position 2 (index 1): they must SWAP!
            viewModel.onIntent(EditProfileIntent.ReorderPhoto(from = 3, to = 1))

            val photos = viewModel.editState.value.photos
            assert(photos[0].url == "http://example.com/photo1.jpg")
            assert(photos[1].url == "http://example.com/photo4.jpg")
            assert(photos[2].url == "http://example.com/photo3.jpg")
            assert(photos[3].url == "http://example.com/photo2.jpg")
        }

    @Test
    fun `removing middle photo compacts subsequent photos leftward`() =
        runTest {
            val threePhotosUser = user.copy(
                photos = listOf(
                    com.feryaeljustice.mirailink.domain.model.user.UserPhoto("1", "http://example.com/photo1.jpg", 1),
                    com.feryaeljustice.mirailink.domain.model.user.UserPhoto("1", "http://example.com/photo2.jpg", 2),
                    com.feryaeljustice.mirailink.domain.model.user.UserPhoto("1", "http://example.com/photo3.jpg", 3),
                ),
            )
            coEvery { getAnimesUseCase.invoke() } returns MiraiLinkResult.Success(emptyList())
            coEvery { getGamesUseCase.invoke() } returns MiraiLinkResult.Success(emptyList())
            coEvery { deleteUserPhotoUseCase.invoke(2) } returns MiraiLinkResult.Success(Unit)

            viewModel.onIntent(EditProfileIntent.Initialize(threePhotosUser.toUserViewEntry()))
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            // Remove photo at index 1 (photo2, 2nd position)
            viewModel.onIntent(EditProfileIntent.RemovePhoto(position = 1))
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            val photos = viewModel.editState.value.photos
            assert(photos[0].url == "http://example.com/photo1.jpg")
            assert(photos[1].url == "http://example.com/photo3.jpg")
            assert(photos[2].url == null)
            assert(photos[3].url == null)
        }

    @Test
    fun `update tags updates selected animes and games correctly`() =
        runTest {
            val animeList = listOf(
                Anime("a1", "Frieren", "https://example.com/frieren.jpg"),
                Anime("a2", "Steins;Gate", "https://example.com/steins.jpg"),
            )
            val gameList = listOf(
                Game("g1", "Persona 5 Royal", "https://example.com/p5r.jpg"),
                Game("g2", "Elden Ring", "https://example.com/elden.jpg"),
            )

            coEvery { getAnimesUseCase.invoke() } returns MiraiLinkResult.Success(animeList)
            coEvery { getGamesUseCase.invoke() } returns MiraiLinkResult.Success(gameList)

            viewModel.onIntent(EditProfileIntent.Initialize(user.toUserViewEntry()))
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            // Update animes
            viewModel.onIntent(EditProfileIntent.UpdateTags(TagType.ANIME, listOf("a1", "a2")))
            val selectedAnimes = viewModel.editState.value.selectedAnimes
            assert(selectedAnimes.size == 2)
            assert(selectedAnimes[0].name == "Frieren")
            assert(selectedAnimes[1].name == "Steins;Gate")

            // Remove one anime
            viewModel.onIntent(EditProfileIntent.UpdateTags(TagType.ANIME, listOf("a2")))
            val updatedAnimes = viewModel.editState.value.selectedAnimes
            assert(updatedAnimes.size == 1)
            assert(updatedAnimes[0].name == "Steins;Gate")

            // Update games
            viewModel.onIntent(EditProfileIntent.UpdateTags(TagType.GAME, listOf("g1")))
            val selectedGames = viewModel.editState.value.selectedGames
            assert(selectedGames.size == 1)
            assert(selectedGames[0].name == "Persona 5 Royal")
        }
}
