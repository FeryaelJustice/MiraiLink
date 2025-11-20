// Author: Feryael Justice
// Date: 2025-11-08

package com.feryaeljustice.mirailink.data.datasource

import com.feryaeljustice.mirailink.core.UnitTest
import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.remote.UsersApiService
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
class UsersRemoteDataSourceTest : UnitTest() {

    private val usersApiService: UsersApiService by inject()
    private val usersRemoteDataSource: UsersRemoteDataSource by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                single { mockk<UsersApiService>() }
                single { UsersRemoteDataSource(get()) }
            },
        )
    }

    @Before
    override fun setUp() {
        super.setUp()
    }

    @Test
    fun `getUsers should return list of users on success`() = runTest {
        // Given
        val userList = listOf(UserDto(id = "1", username = "testuser", nickname = "Test User"))
        coEvery { usersApiService.getUsers() } returns userList

        // When
        val result = usersRemoteDataSource.getUsers()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(userList, (result as MiraiLinkResult.Success).data)
        coVerify { usersApiService.getUsers() }
    }
}
