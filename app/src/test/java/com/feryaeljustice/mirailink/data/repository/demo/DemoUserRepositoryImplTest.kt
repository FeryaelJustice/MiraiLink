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
class DemoUserRepositoryImplTest {

    private lateinit var database: MiraiLinkDemoDatabase
    private lateinit var seeder: DemoDataSeeder
    private lateinit var repository: DemoUserRepositoryImpl

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            MiraiLinkDemoDatabase::class.java,
        ).allowMainThreadQueries().build()
        seeder = DemoDataSeeder(database)
        repository = DemoUserRepositoryImpl(database, seeder)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `getCurrentUser returns demo user with initial data`() = runTest {
        val result = repository.getCurrentUser()

        assertThat(result).isInstanceOf(MiraiLinkResult.Success::class.java)
        val user = (result as MiraiLinkResult.Success).data
        assertThat(user.id).isEqualTo(DemoDataSeeder.DEMO_USER_ID)
        assertThat(user.nickname).isEqualTo("Hikari")
        assertThat(user.animes).isNotEmpty()
        assertThat(user.games).isNotEmpty()
    }

    @Test
    fun `updateProfile modifies stored demo user`() = runTest {
        repository.getCurrentUser()

        val updateResult = repository.updateProfile(
            nickname = "Hikari_Modificada",
            bio = "Nueva bio demo",
            gender = "Mujer",
            birthdate = "2003-05-14",
            animesJson = "[]",
            gamesJson = "[]",
            photoUris = emptyList(),
            existingPhotoUrls = emptyList(),
        )

        assertThat(updateResult).isInstanceOf(MiraiLinkResult.Success::class.java)

        val reloaded = (repository.getCurrentUser() as MiraiLinkResult.Success).data
        assertThat(reloaded.nickname).isEqualTo("Hikari_Modificada")
        assertThat(reloaded.bio).isEqualTo("Nueva bio demo")
    }

    @Test
    fun `getUserById returns feed user details when requested`() = runTest {
        repository.getCurrentUser()

        val result = repository.getUserById("demo_user_1")
        assertThat(result).isInstanceOf(MiraiLinkResult.Success::class.java)
        val user = (result as MiraiLinkResult.Success).data
        assertThat(user.nickname).isEqualTo("Aoi")
    }

    @Test
    fun `autologin and login succeed immediately in demo mode`() = runTest {
        val autoResult = repository.autologin()
        assertThat(autoResult).isInstanceOf(MiraiLinkResult.Success::class.java)
        assertThat((autoResult as MiraiLinkResult.Success).data).isEqualTo(DemoDataSeeder.DEMO_USER_ID)

        val loginResult = repository.login("test@test.com", "test", "pass")
        assertThat(loginResult).isInstanceOf(MiraiLinkResult.Success::class.java)
    }
}
