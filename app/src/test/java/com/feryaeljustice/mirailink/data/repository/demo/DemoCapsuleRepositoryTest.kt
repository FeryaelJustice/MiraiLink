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
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
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

    private suspend fun action(state: CrystalCapsule, type: String, category: String? = null, text: String? = null, answer: String? = null): CrystalCapsule {
        val result = repository.act(
            state.id,
            CapsuleAction(
                actionId = UUID.randomUUID().toString(),
                expectedRevision = state.revision,
                type = type,
                category = category,
                missionId = state.question?.instanceId,
                text = text,
                answer = answer,
            ),
        )
        return (result as MiraiLinkResult.Success).data
    }

    @Test fun `chat messages do not advance capsule progress which is strictly reserved for questions`() = runTest {
        DemoDataSeeder(db).resetDemoData()
        db.startCapsule(peer)
        val own = DemoDataSeeder.DEMO_USER_ID
        val start = System.currentTimeMillis()
        for ((index, sender) in listOf(own, peer, peer, own).withIndex()) {
            db.chatDao().insertMessage(
                com.feryaeljustice.mirailink.data.local.demo.entity.DemoMessageEntity(
                    UUID.randomUUID().toString(), "chat_$peer", sender, if (sender == own) peer else own, "turn $index", start + index,
                ),
            )
            repository.history(peer)
        }
        assertEquals(0, repository.cached(peer)!!.progress)
    }

    @Test fun `proposing question with answer awards 1 point and duplicate actions are idempotent`() = runTest {
        DemoDataSeeder(db).resetDemoData()
        db.startCapsule(peer)
        val current = repository.cached(peer)!!
        val action = CapsuleAction(UUID.randomUUID().toString(), current.revision, "question", category = "gaming", text = "Mi respuesta favorita", answer = "Mi respuesta favorita")
        val first = (repository.act(current.id, action) as MiraiLinkResult.Success).data
        val second = (repository.act(current.id, action) as MiraiLinkResult.Success).data
        assertEquals(first, second)
        assertEquals(1, second.progress)
        assertEquals(1, second.completedQuestions.size)
        assertTrue(db.capsulePresentation(peer)!!.veiled)
    }

    @Test fun `reaching 4 points unlocks capsule and unveils photos`() = runTest {
        DemoDataSeeder(db).resetDemoData()
        db.startCapsule(peer)
        var state = repository.cached(peer)!!
        for (i in 1..4) {
            state = action(state, "question", "anime", text = "Respuesta $i", answer = "Respuesta $i")
            assertEquals(i, state.progress)
        }
        assertEquals("revealed", state.status)
        assertEquals(4, state.progress)
        assertFalse(db.capsulePresentation(peer)!!.veiled)
    }

    @Test fun `undo and relink preserve progress and require resuming before missions`() = runTest {
        DemoDataSeeder(db).resetDemoData()
        db.startCapsule(peer)
        var state = action(repository.cached(peer)!!, "question", "anime", text = "Frieren", answer = "Frieren")
        assertEquals(1, state.progress)
        db.cancelCapsule(peer)
        assertEquals("cancelled", repository.cached(peer)!!.status)
        db.startCapsule(peer)
        state = repository.cached(peer)!!
        assertEquals("paused", state.status)
        assertEquals(1, state.progress)
        val forbidden = repository.act(state.id, CapsuleAction(UUID.randomUUID().toString(), state.revision, "question", "family", text = "test", answer = "test"))
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

    @Test fun `capsule question respects language and records localized text in history`() = runTest {
        DemoDataSeeder(db).resetDemoData()
        db.startCapsule(peer)
        val current = repository.cached(peer)!!
        val action = CapsuleAction(
            actionId = UUID.randomUUID().toString(),
            expectedRevision = current.revision,
            type = "question",
            category = "gaming",
            text = "My favorite game",
            answer = "Chrono Trigger",
            language = "en",
        )
        val state = (repository.act(current.id, action) as MiraiLinkResult.Success).data
        assertEquals(1, state.progress)
        val completed = state.completedQuestions.first()
        assertEquals("en", completed.questionLanguage)
        assertEquals("en", completed.authorLanguage)
        assertEquals("Chrono Trigger", completed.authorAnswer)
        assertTrue(completed.displayText().isNotEmpty())
    }
}
