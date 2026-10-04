package com.feryaeljustice.mirailink.domain.usecase.swipe

import com.feryaeljustice.mirailink.domain.error.SubscriptionError
import com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionPlanType
import com.feryaeljustice.mirailink.domain.model.swipe.UndoQuota
import com.feryaeljustice.mirailink.domain.model.swipe.UndoSwipeResult
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.repository.SwipeRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UndoSwipeUseCaseTest {

    private lateinit var repository: SwipeRepository
    private lateinit var undoSwipeUseCase: UndoSwipeUseCase
    private lateinit var getUndoQuotaUseCase: GetUndoQuotaUseCase

    private val fakeQuota = UndoQuota(
        tier = SubscriptionPlanType.FREE,
        maxUndos = 1,
        usedUndos = 0,
        remainingUndos = 1,
        resetsAt = null,
        hasUndoableSwipe = true,
        canUndo = true,
    )

    private val fakeUser = User(
        id = "f47ac10b-58cc-4372-a567-0e02b2c3d479",
        username = "aoi_test",
        nickname = "Aoi",
        email = "aoi@example.com",
        phoneNumber = null,
        bio = "Testing undo",
        gender = "FEMALE",
        birthdate = "2000-01-01",
        animes = emptyList(),
        games = emptyList(),
        photos = emptyList(),
    )

    @Before
    fun setUp() {
        repository = mockk()
        undoSwipeUseCase = UndoSwipeUseCase(repository)
        getUndoQuotaUseCase = GetUndoQuotaUseCase(repository)
    }

    @Test
    fun `undoSwipe calls repository with targetUserId and returns success`() = runTest {
        val expectedResult = UndoSwipeResult(
            user = fakeUser,
            actionUndone = "like",
            quota = fakeQuota.copy(usedUndos = 1, remainingUndos = 0, canUndo = false),
        )
        coEvery { repository.undoSwipe("user-123") } returns MiraiLinkResult.Success(expectedResult)

        val result = undoSwipeUseCase("user-123")

        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(expectedResult, (result as MiraiLinkResult.Success).data)
        coVerify(exactly = 1) { repository.undoSwipe("user-123") }
    }

    @Test
    fun `undoSwipe returns error when daily undo limit is reached`() = runTest {
        coEvery { repository.undoSwipe(null) } returns MiraiLinkResult.Error(SubscriptionError.DAILY_UNDO_LIMIT_REACHED)

        val result = undoSwipeUseCase()

        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(SubscriptionError.DAILY_UNDO_LIMIT_REACHED, (result as MiraiLinkResult.Error).error)
        coVerify(exactly = 1) { repository.undoSwipe(null) }
    }

    @Test
    fun `getUndoQuota delegates to repository and returns current quota`() = runTest {
        coEvery { repository.getUndoQuota() } returns MiraiLinkResult.Success(fakeQuota)

        val result = getUndoQuotaUseCase()

        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(fakeQuota, (result as MiraiLinkResult.Success).data)
        coVerify(exactly = 1) { repository.getUndoQuota() }
    }
}
