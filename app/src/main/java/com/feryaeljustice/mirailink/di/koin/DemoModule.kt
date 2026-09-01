package com.feryaeljustice.mirailink.di.koin

import androidx.room.Room
import com.feryaeljustice.mirailink.data.demo.DemoModeManager
import com.feryaeljustice.mirailink.data.local.demo.DemoDataSeeder
import com.feryaeljustice.mirailink.data.local.demo.MiraiLinkDemoDatabase
import com.feryaeljustice.mirailink.data.repository.demo.DemoChatRepositoryImpl
import com.feryaeljustice.mirailink.data.repository.demo.DemoMatchRepositoryImpl
import com.feryaeljustice.mirailink.data.repository.demo.DemoSwipeRepositoryImpl
import com.feryaeljustice.mirailink.data.repository.demo.DemoUserRepositoryImpl
import com.feryaeljustice.mirailink.di.koin.Qualifiers.ApplicationScope
import com.feryaeljustice.mirailink.di.koin.Qualifiers.Demo
import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import com.feryaeljustice.mirailink.domain.repository.MatchRepository
import com.feryaeljustice.mirailink.domain.repository.SwipeRepository
import com.feryaeljustice.mirailink.domain.repository.UserRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val demoModule =
    module {
        single<MiraiLinkDemoDatabase> {
            Room.databaseBuilder(
                androidContext(),
                MiraiLinkDemoDatabase::class.java,
                MiraiLinkDemoDatabase.DATABASE_NAME,
            ).fallbackToDestructiveMigration().build()
        }

        single { get<MiraiLinkDemoDatabase>().userDao() }
        single { get<MiraiLinkDemoDatabase>().matchDao() }
        single { get<MiraiLinkDemoDatabase>().chatDao() }

        single { DemoDataSeeder(database = get()) }
        single { DemoModeManager(seeder = get(), scope = get(ApplicationScope)) }

        single<UserRepository>(Demo) { DemoUserRepositoryImpl(database = get(), seeder = get()) }
        single<MatchRepository>(Demo) { DemoMatchRepositoryImpl(database = get()) }
        single<SwipeRepository>(Demo) { DemoSwipeRepositoryImpl(database = get(), seeder = get()) }
        single<ChatRepository>(Demo) { DemoChatRepositoryImpl(database = get(), scope = get(ApplicationScope)) }
    }
