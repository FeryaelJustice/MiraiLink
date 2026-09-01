package com.feryaeljustice.mirailink.data.repository.demo

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.feryaeljustice.mirailink.data.local.demo.DemoDataSeeder
import com.feryaeljustice.mirailink.data.local.demo.MiraiLinkDemoDatabase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
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
class DemoSwipeRepositoryImplTest {

    private lateinit var database: MiraiLinkDemoDatabase
    private lateinit var seeder: DemoDataSeeder
    private lateinit var repository: DemoSwipeRepositoryImpl

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            MiraiLinkDemoDatabase::class.java,
        ).allowMainThreadQueries().build()
        seeder = DemoDataSeeder(database)
        repository = DemoSwipeRepositoryImpl(database, seeder)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `getFeed returns list of unswiped demo users`() = runTest {
        val result = repository.getFeed()

        assertThat(result).isInstanceOf(MiraiLinkResult.Success::class.java)
        val users = (result as MiraiLinkResult.Success).data
        assertThat(users).isNotEmpty()
    }

    @Test
    fun `likeUser creates match and initial chat message`() = runTest {
        val targetUserId = "demo_user_1"

        val likeResult = repository.likeUser(targetUserId)

        assertThat(likeResult).isInstanceOf(MiraiLinkResult.Success::class.java)
        assertThat((likeResult as MiraiLinkResult.Success).data).isTrue()

        val match = database.matchDao().getMatchByUserId(targetUserId)
        assertThat(match).isNotNull()

        val chat = database.chatDao().getChatByUserId(targetUserId)
        assertThat(chat).isNotNull()
    }

    @Test
    fun `dislikeUser marks user as disliked without match`() = runTest {
        val targetUserId = "demo_user_4"

        val dislikeResult = repository.dislikeUser(targetUserId)

        assertThat(dislikeResult).isInstanceOf(MiraiLinkResult.Success::class.java)

        val match = database.matchDao().getMatchByUserId(targetUserId)
        assertThat(match).isNull()
    }
}
