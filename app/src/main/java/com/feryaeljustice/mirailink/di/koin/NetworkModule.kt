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
import com.feryaeljustice.mirailink.data.remote.interceptor.ImageDomainSecurityInterceptor
import com.feryaeljustice.mirailink.di.koin.Qualifiers.BaseApiUrl
import com.feryaeljustice.mirailink.di.koin.Qualifiers.BaseUrl
import com.feryaeljustice.mirailink.di.koin.Qualifiers.ImageOkHttpClient
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import java.util.Locale

val networkModule =
    module {
        single(BaseUrl) { BuildConfig.MIRAILINK_BASE_URL }
        single(BaseApiUrl) { "${get<String>(BaseUrl)}/api/" }

        single { AuthInterceptor(get<SessionManager>()) }
        single { ImageDomainSecurityInterceptor() }

        single(ImageOkHttpClient) {
            OkHttpClient
                .Builder()
                .apply {
                    if (BuildConfig.DEBUG) {
                        addInterceptor(
                            HttpLoggingInterceptor().apply {
                                level = HttpLoggingInterceptor.Level.HEADERS
                            },
                        )
                    }
                    addInterceptor(get<ImageDomainSecurityInterceptor>())
                    connectTimeout(15, TimeUnit.SECONDS)
                    readTimeout(20, TimeUnit.SECONDS)
                }.build()
        }

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
                    addInterceptor { chain ->
                        val request = chain.request().newBuilder()
                            .header("Accept-Language", Locale.getDefault().toLanguageTag())
                            .build()
                        chain.proceed(request)
                    }
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

        single { get<Retrofit>().create(AppConfigApiService::class.java) }
        single { get<Retrofit>().create(UserApiService::class.java) }
        single { get<Retrofit>().create(UsersApiService::class.java) }
        single { get<Retrofit>().create(TwoFactorApiService::class.java) }
        single { get<Retrofit>().create(SwipeApiService::class.java) }
        single { get<Retrofit>().create(ChatApiService::class.java) }
        single { get<Retrofit>().create(MatchApiService::class.java) }
        single { get<Retrofit>().create(CatalogApiService::class.java) }
        single { get<Retrofit>().create(ReportApiService::class.java) }
        single { get<Retrofit>().create(FeedbackApiService::class.java) }
    }
