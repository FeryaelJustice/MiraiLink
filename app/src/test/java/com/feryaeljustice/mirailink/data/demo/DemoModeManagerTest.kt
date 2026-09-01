package com.feryaeljustice.mirailink.data.demo

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.feryaeljustice.mirailink.data.local.demo.DemoDataSeeder
import com.feryaeljustice.mirailink.data.local.demo.MiraiLinkDemoDatabase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
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
class DemoModeManagerTest {

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
    fun `enableDemoMode sets isDemoMode to true and seeds database`() = runTest {
        val manager = DemoModeManager(seeder, this)
        assertThat(manager.isDemoActive()).isFalse()

        var completed = false
        val job = manager.enableDemoMode {
            completed = true
        }
        job.join()

        assertThat(manager.isDemoActive()).isTrue()
        assertThat(completed).isTrue()

        val profile = database.userDao().getUserProfile(DemoDataSeeder.DEMO_USER_ID)
        assertThat(profile).isNotNull()
    }

    @Test
    fun `disableDemoMode sets isDemoMode to false`() = runTest {
        val manager = DemoModeManager(seeder, this)
        val job = manager.enableDemoMode()
        job.join()
        assertThat(manager.isDemoActive()).isTrue()

        manager.disableDemoMode()
        assertThat(manager.isDemoActive()).isFalse()
    }
}
