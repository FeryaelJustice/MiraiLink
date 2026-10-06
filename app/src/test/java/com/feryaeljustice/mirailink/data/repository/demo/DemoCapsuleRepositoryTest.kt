package com.feryaeljustice.mirailink.data.repository.demo

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.feryaeljustice.mirailink.data.local.demo.*
import com.feryaeljustice.mirailink.domain.model.capsule.CapsuleAction
import com.feryaeljustice.mirailink.domain.model.capsule.CrystalCapsule
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.UUID

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class DemoCapsuleRepositoryTest {
    private lateinit var db: MiraiLinkDemoDatabase
    private lateinit var repository: DemoCapsuleRepository
    private val peer = "demo_user_1"
    @Before fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MiraiLinkDemoDatabase::class.java).allowMainThreadQueries().build()
        repository = DemoCapsuleRepository(db, DemoChatRepositoryImpl(db), context)
    }
    @After fun close() { db.close() }
    private suspend fun action(state: CrystalCapsule, type: String, category: String? = null, text: String? = null): CrystalCapsule {
        val result = repository.act(state.id, CapsuleAction(UUID.randomUUID().toString(), state.revision, type, category, state.question?.instanceId, text))
        return (result as MiraiLinkResult.Success).data
    }
    @Test fun `consecutive peer messages remain one turn across completed exchanges and polling cycles`() = runTest {
        DemoDataSeeder(db).resetDemoData(); db.startCapsule(peer)
        val own = DemoDataSeeder.DEMO_USER_ID
        val start = System.currentTimeMillis()
        for ((index, sender) in listOf(own, peer, peer, own).withIndex()) {
            db.chatDao().insertMessage(com.feryaeljustice.mirailink.data.local.demo.entity.DemoMessageEntity(
                UUID.randomUUID().toString(), "chat_$peer", sender, if (sender == own) peer else own, "turn $index", start + index))
            repository.history(peer)
        }
        assertEquals(1, repository.cached(peer)!!.progress)
    }
    @Test fun `missions persist and retries never duplicate answers or progress`() = runTest {
        DemoDataSeeder(db).resetDemoData()
        db.startCapsule(peer)
        val selected = action(repository.cached(peer)!!, "question", "gaming")
        val answer = CapsuleAction(UUID.randomUUID().toString(), selected.revision, "answer", missionId = selected.question!!.instanceId, text = "A co-op adventure")
        val first = (repository.act(selected.id, answer) as MiraiLinkResult.Success).data
        val second = (repository.act(selected.id, answer) as MiraiLinkResult.Success).data
        assertEquals(first, second)
        assertEquals(2, second.progress)
        assertEquals(1, db.chatDao().getMessagesBetween(DemoDataSeeder.DEMO_USER_ID, peer).count { it.id == answer.actionId })
        assertEquals(2, (repository.history(peer) as MiraiLinkResult.Success).data.capsule!!.progress)
        assertTrue(db.capsulePresentation(peer)!!.veiled)
    }
    @Test fun `undo and relink preserve progress and require resuming before missions`() = runTest {
        DemoDataSeeder(db).resetDemoData(); db.startCapsule(peer)
        var state = action(repository.cached(peer)!!, "question", "anime")
        state = action(state, "answer", text = "Frieren")
        db.cancelCapsule(peer)
        assertEquals("cancelled", repository.cached(peer)!!.status)
        db.startCapsule(peer)
        state = repository.cached(peer)!!
        assertEquals("paused", state.status); assertEquals(2, state.progress)
        val forbidden = repository.act(state.id, CapsuleAction(UUID.randomUUID().toString(), state.revision, "question", "family"))
        assertTrue(forbidden is MiraiLinkResult.Error)
        state = action(state, "resume")
        state = action(state, "request_reveal")
        assertEquals("revealed", state.status)
        assertFalse(db.capsulePresentation(peer)!!.veiled)
    }
    @Test fun `reset clears capsules without turning old classic matches into capsules`() = runTest {
        val seeder = DemoDataSeeder(db)
        seeder.resetDemoData()
        val classic = db.matchDao().getAllMatches().first().userId
        assertNull(db.capsulePresentation(classic, true))
        db.startCapsule(peer)
        seeder.resetDemoData()
        assertNull(repository.cached(peer))
        assertTrue(db.userDao().getAllFeedUsers().isNotEmpty())
    }
}
