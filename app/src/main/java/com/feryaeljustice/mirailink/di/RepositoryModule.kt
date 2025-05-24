package com.feryaeljustice.mirailink.di

import com.feryaeljustice.mirailink.data.repository.ChatRepositoryImpl
import com.feryaeljustice.mirailink.data.repository.MatchRepositoryImpl
import com.feryaeljustice.mirailink.data.repository.SwipeRepositoryImpl
import com.feryaeljustice.mirailink.data.repository.UserRepositoryImpl
import com.feryaeljustice.mirailink.data.repository.UsersRepositoryImpl
import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import com.feryaeljustice.mirailink.domain.repository.MatchRepository
import com.feryaeljustice.mirailink.domain.repository.SwipeRepository
import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.repository.UsersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindUsersRepository(impl: UsersRepositoryImpl): UsersRepository

    @Binds
    @Singleton
    abstract fun bindSwipeRepository(impl: SwipeRepositoryImpl): SwipeRepository

    @Binds
    @Singleton
    abstract fun bindChatRepository(impl: ChatRepositoryImpl): ChatRepository

    @Binds
    @Singleton
    abstract fun bindMatchRepository(impl: MatchRepositoryImpl): MatchRepository
}