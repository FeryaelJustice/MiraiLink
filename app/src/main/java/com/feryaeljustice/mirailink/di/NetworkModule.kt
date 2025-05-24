package com.feryaeljustice.mirailink.di

import com.feryaeljustice.mirailink.core.SessionManager
import com.feryaeljustice.mirailink.data.local.TokenManager
import com.feryaeljustice.mirailink.data.remote.ChatApiService
import com.feryaeljustice.mirailink.data.remote.SwipeApiService
import com.feryaeljustice.mirailink.data.remote.UserApiService
import com.feryaeljustice.mirailink.data.remote.UsersApiService
import com.feryaeljustice.mirailink.data.remote.interceptor.AuthInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Base URL for the API.
     * This is the URL that will be used to make requests to the API.
     * 10.0.2.2 inside emulator, local ip over wifi (with port)
     * in production domain remove :3000
     */
    @Provides
    @Singleton
    @Named("BaseUrl")
    fun provideBaseUrl(): String = "http://192.168.1.132:3000"

    @Provides
    @Singleton
    @Named("BaseApiUrl")
    fun provideBaseApiUrl(@Named("BaseUrl") baseUrl: String): String = "${baseUrl}/api/"

    @Provides
    @Singleton
    fun provideSessionManager(): SessionManager = SessionManager()

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        tokenManager: TokenManager,
        sessionManager: SessionManager
    ): AuthInterceptor {
        return AuthInterceptor(tokenManager = tokenManager, sessionManager = sessionManager)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideRetrofit(
        @Named("BaseApiUrl") baseApiUrl: String,
        json: Json,
        client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseApiUrl)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @Provides
    @Singleton
    fun provideUserApiService(retrofit: Retrofit): UserApiService =
        retrofit.create(UserApiService::class.java)

    @Provides
    @Singleton
    fun provideUsersApiService(retrofit: Retrofit): UsersApiService =
        retrofit.create(UsersApiService::class.java)

    @Provides
    @Singleton
    fun provideSwipeApiService(retrofit: Retrofit): SwipeApiService =
        retrofit.create(SwipeApiService::class.java)

    @Provides
    @Singleton
    fun provideChatApiService(retrofit: Retrofit): ChatApiService =
        retrofit.create(ChatApiService::class.java)
}