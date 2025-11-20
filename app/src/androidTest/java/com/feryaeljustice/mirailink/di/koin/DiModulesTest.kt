package com.feryaeljustice.mirailink.di.koin

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
import org.junit.Assert
import org.junit.Test
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named
import retrofit2.Retrofit

class DiModulesTest : KoinComponent {
    @Test
    fun testCryptoModuleInjections() {
        Assert.assertNotNull(get<SecretKeyProvider>())
        Assert.assertNotNull(get<Json>())
    }

    @Test
    fun testNetworkModuleInjections() {
        Assert.assertNotNull(get<AppConfigApiService>())
        Assert.assertNotNull(get<UserApiService>())
        Assert.assertNotNull(get<UsersApiService>())
        Assert.assertNotNull(get<TwoFactorApiService>())
        Assert.assertNotNull(get<SwipeApiService>())
        Assert.assertNotNull(get<ChatApiService>())
        Assert.assertNotNull(get<MatchApiService>())
        Assert.assertNotNull(get<CatalogApiService>())
        Assert.assertNotNull(get<ReportApiService>())
        Assert.assertNotNull(get<FeedbackApiService>())
        Assert.assertNotNull(get<OkHttpClient>())
        Assert.assertNotNull(get<Retrofit>())
        Assert.assertNotNull(get<AuthInterceptor>())
        Assert.assertNotNull(get<String>(named("BaseUrl")))
        Assert.assertNotNull(get<String>(named("BaseApiUrl")))
    }

    @Test
    fun testDispatchersModuleInjections() {
        Assert.assertNotNull(get<CoroutineDispatcher>(named("IoDispatcher")))
        Assert.assertNotNull(get<CoroutineDispatcher>(named("MainDispatcher")))
    }

    @Test
    fun testDataStoreModuleInjections() {
        Assert.assertNotNull(get<DataStore<AppPrefs>>(named("PrefsDataStore")))
        Assert.assertNotNull(get<DataStore<Session>>(named("SessionDataStore")))
    }

    @Test
    fun testRepositoryModuleInjections() {
        Assert.assertNotNull(get<AppConfigRepository>())
        Assert.assertNotNull(get<OnboardingRepository>())
        Assert.assertNotNull(get<UserRepository>())
        Assert.assertNotNull(get<UsersRepository>())
        Assert.assertNotNull(get<TwoFactorRepository>())
        Assert.assertNotNull(get<SwipeRepository>())
        Assert.assertNotNull(get<ChatRepository>())
        Assert.assertNotNull(get<MatchRepository>())
        Assert.assertNotNull(get<CatalogRepository>())
        Assert.assertNotNull(get<ReportRepository>())
        Assert.assertNotNull(get<FeedbackRepository>())
    }

    @Test
    fun testSocketModuleInjections() {
        Assert.assertNotNull(get<SocketService>())
    }

    @Test
    fun testLoggerModuleInjections() {
        Assert.assertNotNull(get<Logger>())
    }

    @Test
    fun testDataModuleInjections() {
        Assert.assertNotNull(get<SessionManager>())
        Assert.assertNotNull(get<MiraiLinkPrefs>())
        Assert.assertNotNull(get<AppConfigRemoteDataSource>())
        Assert.assertNotNull(get<UserRemoteDataSource>())
        Assert.assertNotNull(get<UsersRemoteDataSource>())
        Assert.assertNotNull(get<TwoFactorRemoteDataSource>())
        Assert.assertNotNull(get<SwipeRemoteDataSource>())
        Assert.assertNotNull(get<ChatRemoteDataSource>())
        Assert.assertNotNull(get<MatchRemoteDataSource>())
        Assert.assertNotNull(get<CatalogRemoteDataSource>())
        Assert.assertNotNull(get<ReportRemoteDataSource>())
        Assert.assertNotNull(get<FeedbackRemoteDatasource>())
    }

    @Test
    fun testTelemetryModuleInjections() {
        Assert.assertNotNull(get<FirebaseAnalytics>())
        Assert.assertNotNull(get<FirebaseCrashlytics>())
        Assert.assertNotNull(get<AnalyticsTracker>())
        Assert.assertNotNull(get<CrashReporter>())
    }
}
