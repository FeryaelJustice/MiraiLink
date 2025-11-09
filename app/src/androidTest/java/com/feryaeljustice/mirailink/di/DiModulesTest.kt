// Author: Feryael Justice
// Date: 2024-08-02

package com.feryaeljustice.mirailink.di

import androidx.datastore.core.DataStore
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
import com.feryaeljustice.mirailink.data.datastore.crypto.SecretKeyProvider
import com.feryaeljustice.mirailink.data.model.local.datastore.AppPrefs
import com.feryaeljustice.mirailink.data.model.local.datastore.Session
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
import com.feryaeljustice.mirailink.data.remote.interceptor.AuthInterceptor
import com.feryaeljustice.mirailink.data.remote.socket.SocketService
import com.feryaeljustice.mirailink.di.koin.createKoinTestRule
import com.feryaeljustice.mirailink.di.koin.testDataStoreModule
import com.feryaeljustice.mirailink.domain.repository.AppConfigRepository
import com.feryaeljustice.mirailink.domain.repository.CatalogRepository
import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import com.feryaeljustice.mirailink.domain.repository.FeedbackRepository
import com.feryaeljustice.mirailink.domain.repository.MatchRepository
import com.feryaeljustice.mirailink.domain.repository.OnboardingRepository
import com.feryaeljustice.mirailink.domain.repository.ReportRepository
import com.feryaeljustice.mirailink.domain.repository.SwipeRepository
import com.feryaeljustice.mirailink.domain.repository.TwoFactorRepository
import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.repository.UsersRepository
import com.feryaeljustice.mirailink.domain.telemetry.AnalyticsTracker
import com.feryaeljustice.mirailink.domain.telemetry.CrashReporter
import com.feryaeljustice.mirailink.domain.util.Logger
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named
import retrofit2.Retrofit

class DiModulesTest : KoinComponent {

    @get:Rule
    val koinTestRule = createKoinTestRule(listOf(testDataStoreModule))

    @Test
    fun testCryptoModuleInjections() {
        assertNotNull(get<SecretKeyProvider>())
        assertNotNull(get<Json>())
    }

    @Test
    fun testNetworkModuleInjections() {
        assertNotNull(get<AppConfigApiService>())
        assertNotNull(get<UserApiService>())
        assertNotNull(get<UsersApiService>())
        assertNotNull(get<TwoFactorApiService>())
        assertNotNull(get<SwipeApiService>())
        assertNotNull(get<ChatApiService>())
        assertNotNull(get<MatchApiService>())
        assertNotNull(get<CatalogApiService>())
        assertNotNull(get<ReportApiService>())
        assertNotNull(get<FeedbackApiService>())
        assertNotNull(get<OkHttpClient>())
        assertNotNull(get<Retrofit>())
        assertNotNull(get<AuthInterceptor>())
        assertNotNull(get<String>(named("BaseUrl")))
        assertNotNull(get<String>(named("BaseApiUrl")))
    }

    @Test
    fun testDispatchersModuleInjections() {
        assertNotNull(get<CoroutineDispatcher>(named("IoDispatcher")))
        assertNotNull(get<CoroutineDispatcher>(named("MainDispatcher")))
    }

    @Test
    fun testDataStoreModuleInjections() {
        assertNotNull(get<DataStore<AppPrefs>>(named("PrefsDataStore")))
        assertNotNull(get<DataStore<Session>>(named("SessionDataStore")))
    }

    @Test
    fun testRepositoryModuleInjections() {
        assertNotNull(get<AppConfigRepository>())
        assertNotNull(get<OnboardingRepository>())
        assertNotNull(get<UserRepository>())
        assertNotNull(get<UsersRepository>())
        assertNotNull(get<TwoFactorRepository>())
        assertNotNull(get<SwipeRepository>())
        assertNotNull(get<ChatRepository>())
        assertNotNull(get<MatchRepository>())
        assertNotNull(get<CatalogRepository>())
        assertNotNull(get<ReportRepository>())
        assertNotNull(get<FeedbackRepository>())
    }

    @Test
    fun testSocketModuleInjections() {
        assertNotNull(get<SocketService>())
    }

    @Test
    fun testLoggerModuleInjections() {
        assertNotNull(get<Logger>())
    }

    @Test
    fun testDataModuleInjections() {
        assertNotNull(get<SessionManager>())
        assertNotNull(get<MiraiLinkPrefs>())
        assertNotNull(get<AppConfigRemoteDataSource>())
        assertNotNull(get<UserRemoteDataSource>())
        assertNotNull(get<UsersRemoteDataSource>())
        assertNotNull(get<TwoFactorRemoteDataSource>())
        assertNotNull(get<SwipeRemoteDataSource>())
        assertNotNull(get<ChatRemoteDataSource>())
        assertNotNull(get<MatchRemoteDataSource>())
        assertNotNull(get<CatalogRemoteDataSource>())
        assertNotNull(get<ReportRemoteDataSource>())
        assertNotNull(get<FeedbackRemoteDatasource>())
    }

    @Test
    fun testTelemetryModuleInjections() {
        assertNotNull(get<FirebaseAnalytics>())
        assertNotNull(get<FirebaseCrashlytics>())
        assertNotNull(get<AnalyticsTracker>())
        assertNotNull(get<CrashReporter>())
    }
}
