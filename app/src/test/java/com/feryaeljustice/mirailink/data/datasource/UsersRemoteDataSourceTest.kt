// Author: Feryael Justice
// Date: 2025-11-01

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
import org.junit.Test

@ExperimentalCoroutinesApi
class UsersRemoteDataSourceTest : UnitTest() {

    private lateinit var usersApiService: UsersApiService
    private lateinit var usersRemoteDataSource: UsersRemoteDataSource

    @Before
    override fun setUp() {
        super.setUp()
        usersApiService = mockk()
        usersRemoteDataSource = UsersRemoteDataSource(usersApiService)
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