package com.feryaeljustice.mirailink.data.local.demo

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class DemoDatabaseTest {

    private lateinit var database: MiraiLinkDemoDatabase
    private lateinit var seeder: DemoDataSeeder

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            MiraiLinkDemoDatabase::class.java,
        ).allowMainThreadQueries().build()
        seeder = DemoDataSeeder(database)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `seeder populates initial demo user profile`() = runTest {
        seeder.resetDemoData()

        val profile = database.userDao().getUserProfile(DemoDataSeeder.DEMO_USER_ID)
        assertThat(profile).isNotNull()
        assertThat(profile?.nickname).isEqualTo("Hikari")
        assertThat(profile?.username).isEqualTo("hikari_demo")
    }

    @Test
    fun `seeder populates feed users and matches`() = runTest {
        seeder.resetDemoData()

        val feedUsers = database.userDao().getAllFeedUsers()
        assertThat(feedUsers).isNotEmpty()
        assertThat(feedUsers.size).isAtLeast(5)

        val matches = database.matchDao().getAllMatches()
        assertThat(matches).isNotEmpty()
        assertThat(matches.size).isAtLeast(2)
    }

    @Test
    fun `seeder populates initial chats and messages`() = runTest {
        seeder.resetDemoData()

        val chats = database.chatDao().getAllChats()
        assertThat(chats).isNotEmpty()

        val messagesSakura = database.chatDao().getMessagesBetween(
            DemoDataSeeder.DEMO_USER_ID,
            "demo_user_3",
        )
        assertThat(messagesSakura).isNotEmpty()
        assertThat(messagesSakura.first().content).contains("Frieren")
    }

    @Test
    fun `user dao updates profile correctly`() = runTest {
        seeder.resetDemoData()

        val profile = database.userDao().getUserProfile(DemoDataSeeder.DEMO_USER_ID)!!
        val updated = profile.copy(bio = "Nueva biografía actualizada de prueba")
        database.userDao().updateUserProfile(updated)

        val reloaded = database.userDao().getUserProfile(DemoDataSeeder.DEMO_USER_ID)
        assertThat(reloaded?.bio).isEqualTo("Nueva biografía actualizada de prueba")
    }

    @Test
    fun `match dao marks unseen matches as seen`() = runTest {
        seeder.resetDemoData()

        val unseenBefore = database.matchDao().getUnseenMatches()
        assertThat(unseenBefore).isNotEmpty()

        database.matchDao().markMatchesAsSeen(listOf("demo_user_2"))

        val unseenAfter = database.matchDao().getUnseenMatches()
        assertThat(unseenAfter.none { it.userId == "demo_user_2" }).isTrue()
    }
}
