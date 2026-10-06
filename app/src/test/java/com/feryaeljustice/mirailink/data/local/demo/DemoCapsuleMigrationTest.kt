package com.feryaeljustice.mirailink.data.local.demo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.UUID

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class DemoCapsuleMigrationTest {
    @Test fun `5 to 6 preserves profiles messages and matches`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val name = "capsule-migration-${UUID.randomUUID()}"
        var db = Room.databaseBuilder(context, MiraiLinkDemoDatabase::class.java, name).allowMainThreadQueries().build()
        try {
            DemoDataSeeder(db).resetDemoData()
            val profile = db.userDao().getUserProfile(DemoDataSeeder.DEMO_USER_ID)
            val matches = db.matchDao().getAllMatches()
            val chats = db.chatDao().getAllChats()
            val peer = chats.first().otherUserId
            val messages = db.chatDao().getMessagesBetween(DemoDataSeeder.DEMO_USER_ID, peer)
            db.close()
            // Versions 5 and 6 share every original table. Remove the sole new table to reconstruct version 5.
            SQLiteDatabase.openDatabase(context.getDatabasePath(name).absolutePath, null, SQLiteDatabase.OPEN_READWRITE).use {
                it.execSQL("DROP TABLE demo_capsules")
                it.version = 5
            }
            db = Room.databaseBuilder(context, MiraiLinkDemoDatabase::class.java, name)
                .addMigrations(MiraiLinkDemoDatabase.MIGRATION_5_6).allowMainThreadQueries().build()
            assertEquals(profile, db.userDao().getUserProfile(DemoDataSeeder.DEMO_USER_ID))
            assertEquals(matches, db.matchDao().getAllMatches())
            assertEquals(chats, db.chatDao().getAllChats())
            assertEquals(messages, db.chatDao().getMessagesBetween(DemoDataSeeder.DEMO_USER_ID, peer))
            assertTrue(db.capsuleDao().all().isEmpty())
        } finally { db.close(); context.deleteDatabase(name) }
    }
}
