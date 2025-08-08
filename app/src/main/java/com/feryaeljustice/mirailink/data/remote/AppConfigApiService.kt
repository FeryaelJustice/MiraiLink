package com.feryaeljustice.mirailink.data.remote

import com.feryaeljustice.mirailink.data.model.AppVersionInfoDto
import retrofit2.http.GET

interface AppConfigApiService {
    @GET("app/version/android")
    suspend fun getAndroidAppVersion(): AppVersionInfoDto
}