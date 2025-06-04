package com.feryaeljustice.mirailink.di

import android.content.Context
import com.feryaeljustice.mirailink.data.datasource.ChatRemoteDataSource
import com.feryaeljustice.mirailink.data.datasource.MatchRemoteDataSource
import com.feryaeljustice.mirailink.data.datasource.SwipeRemoteDataSource
import com.feryaeljustice.mirailink.data.datasource.UserRemoteDataSource
import com.feryaeljustice.mirailink.data.datasource.UsersRemoteDataSource
import com.feryaeljustice.mirailink.data.remote.ChatApiService
import com.feryaeljustice.mirailink.data.remote.MatchApiService
import com.feryaeljustice.mirailink.data.remote.SwipeApiService
import com.feryaeljustice.mirailink.data.remote.UserApiService
import com.feryaeljustice.mirailink.data.remote.UsersApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideUserRemoteDataSource(
        api: UserApiService,
        @ApplicationContext context: Context
    ): UserRemoteDataSource =
        UserRemoteDataSource(api, context)

    @Provides
    @Singleton
    fun provideUsersRemoteDataSource(usersApi: UsersApiService): UsersRemoteDataSource =
        UsersRemoteDataSource(usersApi)

    @Provides
    @Singleton
    fun provideSwipeRemoteDatasource(swipeApi: SwipeApiService): SwipeRemoteDataSource =
        SwipeRemoteDataSource(swipeApi)

    @Provides
    @Singleton
    fun provideChatRemoteDatasource(chatApi: ChatApiService): ChatRemoteDataSource =
        ChatRemoteDataSource(chatApi)

    @Provides
    @Singleton
    fun provideMatchRemoteDatasource(matchApi: MatchApiService): MatchRemoteDataSource =
        MatchRemoteDataSource(matchApi)
}