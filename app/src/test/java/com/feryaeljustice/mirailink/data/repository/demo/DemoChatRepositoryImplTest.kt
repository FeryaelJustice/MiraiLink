package com.feryaeljustice.mirailink.data.repository.demo

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.feryaeljustice.mirailink.data.local.demo.DemoDataSeeder
import com.feryaeljustice.mirailink.data.local.demo.MiraiLinkDemoDatabase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class DemoChatRepositoryImplTest {

    private lateinit var database: MiraiLinkDemoDatabase
    private lateinit var seeder: DemoDataSeeder
    private lateinit var repository: DemoChatRepositoryImpl
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            MiraiLinkDemoDatabase::class.java,
        ).allowMainThreadQueries().build()
        seeder = DemoDataSeeder(database)
        repository = DemoChatRepositoryImpl(database, testScope)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `getChatsFromUser returns seeded chat previews`() = runTest {
        seeder.resetDemoData()

        val result = repository.getChatsFromUser()

        assertThat(result).isInstanceOf(MiraiLinkResult.Success::class.java)
        val chats = (result as MiraiLinkResult.Success).data
        assertThat(chats).isNotEmpty()
    }

    @Test
    fun `sendMessageTo inserts user message into database`() = runTest {
        seeder.resetDemoData()
        val targetUserId = "demo_user_3"

        val sendResult = repository.sendMessageTo(targetUserId, "Hola Sakura, me alegra saludarte!")

        assertThat(sendResult).isInstanceOf(MiraiLinkResult.Success::class.java)

        val messages = (repository.getMessagesWith(targetUserId) as MiraiLinkResult.Success).data
        assertThat(messages.any { it.content == "Hola Sakura, me alegra saludarte!" }).isTrue()
    }

    @Test
    fun `markChatAsRead clears unread count in room`() = runTest {
        seeder.resetDemoData()
        val chatId = "chat_sakura_demo"

        repository.markChatAsRead(chatId)

        val chat = database.chatDao().getChatById(chatId)
        assertThat(chat?.unreadCount).isEqualTo(0)
    }
}
