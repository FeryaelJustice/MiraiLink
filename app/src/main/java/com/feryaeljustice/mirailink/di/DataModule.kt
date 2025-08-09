package com.feryaeljustice.mirailink.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.feryaeljustice.mirailink.data.datasource.AppConfigRemoteDataSource
import com.feryaeljustice.mirailink.data.datasource.CatalogRemoteDataSource
import com.feryaeljustice.mirailink.data.datasource.ChatRemoteDataSource
import com.feryaeljustice.mirailink.data.datasource.FeedbackRemoteDatasource
import com.feryaeljustice.mirailink.data.datasource.MatchRemoteDataSource
import com.feryaeljustice.mirailink.data.datasource.ReportRemoteDataSource
import com.feryaeljustice.mirailink.data.datasource.SwipeRemoteDataSource
import com.feryaeljustice.mirailink.data.datasource.TwoFactorRemoteDataSource
import com.feryaeljustice.mirailink.data.datasource.UserRemoteDataSource
import com.feryaeljustice.mirailink.data.datasource.UsersRemoteDataSource
import com.feryaeljustice.mirailink.data.datastore.MiraiLinkPrefs
import com.feryaeljustice.mirailink.data.datastore.SessionManager
import com.feryaeljustice.mirailink.data.remote.AppConfigApiService
import com.feryaeljustice.mirailink.data.remote.CatalogApiService
import com.feryaeljustice.mirailink.data.remote.ChatApiService
import com.feryaeljustice.mirailink.data.remote.FeedbackApiService
import com.feryaeljustice.mirailink.data.remote.MatchApiService
import com.feryaeljustice.mirailink.data.remote.ReportApiService
import com.feryaeljustice.mirailink.data.remote.SwipeApiService
import com.feryaeljustice.mirailink.data.remote.TwoFactorApiService
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
    fun provideSessionManager(
        @SessionDataStore dataStore: DataStore<Preferences>,
    ): SessionManager = SessionManager(
        dataStore = dataStore,
    )

    @Provides
    @Singleton
    fun provideMiraiLinkPrefs(
        @PrefsDataStore dataStore: DataStore<Preferences>
    ): MiraiLinkPrefs = MiraiLinkPrefs(dataStore = dataStore)

    @Provides
    @Singleton
    fun provideAppConfigRemoteDataSource(
        api: AppConfigApiService
    ): AppConfigRemoteDataSource =
        AppConfigRemoteDataSource(api)

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
    fun provideTwoFactorRemoteDataSource(twoFactorApiService: TwoFactorApiService): TwoFactorRemoteDataSource =
        TwoFactorRemoteDataSource(twoFactorApiService)

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

    @Provides
    @Singleton
    fun provideCatalogRemoteDatasource(catalogApi: CatalogApiService): CatalogRemoteDataSource =
        CatalogRemoteDataSource(catalogApi)

    @Provides
    @Singleton
    fun provideReportRemoteDatasource(reportApi: ReportApiService): ReportRemoteDataSource =
        ReportRemoteDataSource(reportApi)

    @Provides
    @Singleton
    fun provideFeedbackRemoteDatasource(feedbackApi: FeedbackApiService): FeedbackRemoteDatasource =
        FeedbackRemoteDatasource(feedbackApi)
}