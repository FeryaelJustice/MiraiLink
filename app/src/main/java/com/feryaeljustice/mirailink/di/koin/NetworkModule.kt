// Author: Feryael Justice
// Date: 2025-11-13

package com.feryaeljustice.mirailink.di.koin

import com.feryaeljustice.mirailink.BuildConfig
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
import com.feryaeljustice.mirailink.data.remote.interceptor.AuthInterceptor
import com.feryaeljustice.mirailink.di.koin.Qualifiers.BaseApiUrl
import com.feryaeljustice.mirailink.di.koin.Qualifiers.BaseUrl
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

val networkModule =
    module {
        single(BaseUrl) { "https://mirailink.xyz" }
        single(BaseApiUrl) { "${get<String>(BaseUrl)}/api/" }

        single { AuthInterceptor(get<SessionManager>()) }

        single {
            OkHttpClient
                .Builder()
                .apply {
                    if (BuildConfig.DEBUG) {
                        addInterceptor(
                            HttpLoggingInterceptor().apply {
                                level = HttpLoggingInterceptor.Level.BODY
                            },
                        )
                    }
                    addInterceptor(get<AuthInterceptor>())
                    connectTimeout(10, TimeUnit.SECONDS)
                    readTimeout(10, TimeUnit.SECONDS)
                    writeTimeout(10, TimeUnit.SECONDS)
                }.build()
        }

        single {
            Retrofit
                .Builder()
                .baseUrl(get<String>(BaseApiUrl))
                .client(get<OkHttpClient>())
                .addConverterFactory(get<Json>().asConverterFactory("application/json".toMediaType()))
                .build()
        }

        single(createdAtStart = true) { get<Retrofit>().create(AppConfigApiService::class.java) }
        single(createdAtStart = true) { get<Retrofit>().create(UserApiService::class.java) }
        single(createdAtStart = true) { get<Retrofit>().create(UsersApiService::class.java) }
        single(createdAtStart = true) { get<Retrofit>().create(TwoFactorApiService::class.java) }
        single(createdAtStart = true) { get<Retrofit>().create(SwipeApiService::class.java) }
        single(createdAtStart = true) { get<Retrofit>().create(ChatApiService::class.java) }
        single(createdAtStart = true) { get<Retrofit>().create(MatchApiService::class.java) }
        single(createdAtStart = true) { get<Retrofit>().create(CatalogApiService::class.java) }
        single(createdAtStart = true) { get<Retrofit>().create(ReportApiService::class.java) }
        single(createdAtStart = true) { get<Retrofit>().create(FeedbackApiService::class.java) }
    }
