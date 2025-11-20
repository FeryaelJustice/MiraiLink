// Author: Feryael Justice
// Date: 2025-11-08

package com.feryaeljustice.mirailink.data.datasource

import com.feryaeljustice.mirailink.core.UnitTest
import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.request.match.MarkMatchAsSeenRequest
import com.feryaeljustice.mirailink.data.model.response.generic.BasicResponse
import com.feryaeljustice.mirailink.data.remote.MatchApiService
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTestRule
import org.koin.test.inject

@ExperimentalCoroutinesApi
class MatchRemoteDataSourceTest : UnitTest() {

    private val matchApiService: MatchApiService by inject()
    private val matchRemoteDataSource: MatchRemoteDataSource by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                single { mockk<MatchApiService>() }
                single { MatchRemoteDataSource(get()) }
            },
        )
    }

    @Before
    override fun setUp() {
        super.setUp()
    }

    @Test
    fun `getMatches should return list of users on success`() = runTest {
        // Given
        val userList = listOf(UserDto(id = "1", username = "testuser", nickname = "Test User"))
        coEvery { matchApiService.getMatches() } returns userList

        // When
        val result = matchRemoteDataSource.getMatches()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(userList, (result as MiraiLinkResult.Success).data)
        coVerify { matchApiService.getMatches() }
    }

    @Test
    fun `getUnseenMatches should return list of users on success`() = runTest {
        // Given
        val userList = listOf(UserDto(id = "2", username = "unseenuser", nickname = "Unseen User"))
        coEvery { matchApiService.getUnseenMatches() } returns userList

        // When
        val result = matchRemoteDataSource.getUnseenMatches()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(userList, (result as MiraiLinkResult.Success).data)
        coVerify { matchApiService.getUnseenMatches() }
    }

    @Test
    fun `markMatchAsSeen should return success`() = runTest {
        // Given
        val matchIds = listOf("1", "2")
        val request = MarkMatchAsSeenRequest(matchIds)
        val response = BasicResponse("Success")
        coEvery { matchApiService.markMatchAsSeen(request) } returns response

        // When
        val result = matchRemoteDataSource.markMatchAsSeen(matchIds)

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        coVerify { matchApiService.markMatchAsSeen(request) }
    }
}
