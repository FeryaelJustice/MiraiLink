// Author: Feryael Justice
// Date: 2025-11-08

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
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidTest
class DiModulesTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var secretKeyProvider: SecretKeyProvider

    @Inject
    lateinit var json: Json

    @Inject
    lateinit var appConfigApiService: AppConfigApiService

    @Inject
    lateinit var userApiService: UserApiService

    @Inject
    lateinit var usersApiService: UsersApiService

    @Inject
    lateinit var twoFactorApiService: TwoFactorApiService

    @Inject
    lateinit var swipeApiService: SwipeApiService

    @Inject
    lateinit var chatApiService: ChatApiService

    @Inject
    lateinit var matchApiService: MatchApiService

    @Inject
    lateinit var catalogApiService: CatalogApiService

    @Inject
    lateinit var reportApiService: ReportApiService

    @Inject
    lateinit var feedbackApiService: FeedbackApiService

    @Inject
    lateinit var okHttpClient: OkHttpClient

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var authInterceptor: AuthInterceptor

    @Inject
    @Named("BaseUrl")
    lateinit var baseUrl: String

    @Inject
    @Named("BaseApiUrl")
    lateinit var baseApiUrl: String

    @Inject
    @IoDispatcher
    lateinit var ioDispatcher: CoroutineDispatcher

    @Inject
    @MainDispatcher
    lateinit var mainDispatcher: CoroutineDispatcher

    @Inject
    @PrefsDataStore
    lateinit var prefsDataStore: DataStore<AppPrefs>

    @Inject
    @SessionDataStore
    lateinit var sessionDataStore: DataStore<Session>

    @Inject
    lateinit var appConfigRepository: AppConfigRepository

    @Inject
    lateinit var onboardingRepository: OnboardingRepository

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var usersRepository: UsersRepository

    @Inject
    lateinit var twoFactorRepository: TwoFactorRepository

    @Inject
    lateinit var swipeRepository: SwipeRepository

    @Inject
    lateinit var chatRepository: ChatRepository

    @Inject
    lateinit var matchRepository: MatchRepository

    @Inject
    lateinit var catalogRepository: CatalogRepository

    @Inject
    lateinit var reportRepository: ReportRepository

    @Inject
    lateinit var feedbackRepository: FeedbackRepository

    @Inject
    lateinit var socketService: SocketService

    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var miraiLinkPrefs: MiraiLinkPrefs

    @Inject
    lateinit var appConfigRemoteDataSource: AppConfigRemoteDataSource

    @Inject
    lateinit var userRemoteDataSource: UserRemoteDataSource

    @Inject
    lateinit var usersRemoteDataSource: UsersRemoteDataSource

    @Inject
    lateinit var twoFactorRemoteDataSource: TwoFactorRemoteDataSource

    @Inject
    lateinit var swipeRemoteDataSource: SwipeRemoteDataSource

    @Inject
    lateinit var chatRemoteDataSource: ChatRemoteDataSource

    @Inject
    lateinit var matchRemoteDataSource: MatchRemoteDataSource

    @Inject
    lateinit var catalogRemoteDataSource: CatalogRemoteDataSource

    @Inject
    lateinit var reportRemoteDataSource: ReportRemoteDataSource

    @Inject
    lateinit var feedbackRemoteDatasource: FeedbackRemoteDatasource

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    @Inject
    lateinit var firebaseCrashlytics: FirebaseCrashlytics

    @Inject
    lateinit var analyticsTracker: AnalyticsTracker

    @Inject
    lateinit var crashReporter: CrashReporter

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testCryptoModuleInjections() {
        assertNotNull(secretKeyProvider)
        assertNotNull(json)
    }

    @Test
    fun testNetworkModuleInjections() {
        assertNotNull(appConfigApiService)
        assertNotNull(userApiService)
        assertNotNull(usersApiService)
        assertNotNull(twoFactorApiService)
        assertNotNull(swipeApiService)
        assertNotNull(chatApiService)
        assertNotNull(matchApiService)
        assertNotNull(catalogApiService)
        assertNotNull(reportApiService)
        assertNotNull(feedbackApiService)
        assertNotNull(okHttpClient)
        assertNotNull(retrofit)
        assertNotNull(authInterceptor)
        assertNotNull(baseUrl)
        assertNotNull(baseApiUrl)
    }

    @Test
    fun testDispatchersModuleInjections() {
        assertNotNull(ioDispatcher)
        assertNotNull(mainDispatcher)
    }

    @Test
    fun testDataStoreModuleInjections() {
        assertNotNull(prefsDataStore)
        assertNotNull(sessionDataStore)
    }

    @Test
    fun testRepositoryModuleInjections() {
        assertNotNull(appConfigRepository)
        assertNotNull(onboardingRepository)
        assertNotNull(userRepository)
        assertNotNull(usersRepository)
        assertNotNull(twoFactorRepository)
        assertNotNull(swipeRepository)
        assertNotNull(chatRepository)
        assertNotNull(matchRepository)
        assertNotNull(catalogRepository)
        assertNotNull(reportRepository)
        assertNotNull(feedbackRepository)
    }

    @Test
    fun testSocketModuleInjections() {
        assertNotNull(socketService)
    }

    @Test
    fun testLoggerModuleInjections() {
        assertNotNull(logger)
    }

    @Test
    fun testDataModuleInjections() {
        assertNotNull(sessionManager)
        assertNotNull(miraiLinkPrefs)
        assertNotNull(appConfigRemoteDataSource)
        assertNotNull(userRemoteDataSource)
        assertNotNull(usersRemoteDataSource)
        assertNotNull(twoFactorRemoteDataSource)
        assertNotNull(swipeRemoteDataSource)
        assertNotNull(chatRemoteDataSource)
        assertNotNull(matchRemoteDataSource)
        assertNotNull(catalogRemoteDataSource)
        assertNotNull(reportRemoteDataSource)
        assertNotNull(feedbackRemoteDatasource)
    }

    @Test
    fun testTelemetryModuleInjections() {
        assertNotNull(firebaseAnalytics)
        assertNotNull(firebaseCrashlytics)
        assertNotNull(analyticsTracker)
        assertNotNull(crashReporter)
    }
}
