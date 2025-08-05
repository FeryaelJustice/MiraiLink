package com.feryaeljustice.mirailink.data.remote

import com.feryaeljustice.mirailink.data.model.response.auth.two_factor.TwoFactorSetupResponse
import com.feryaeljustice.mirailink.data.model.response.auth.two_factor.TwoFactorStatusResponse
import com.feryaeljustice.mirailink.data.model.response.generic.BasicResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TwoFactorApiService {
    @POST("auth/2fa/status")
    suspend fun getTwoFactorStatus(@Body body: Map<String, String>): TwoFactorStatusResponse

    @POST("auth/2fa/setup")
    suspend fun setupTwoFactor(): TwoFactorSetupResponse

    @POST("auth/2fa/verify")
    suspend fun verifyTwoFactor(@Body body: Map<String, String>): Response<Unit>

    @POST("auth/2fa/disable")
    suspend fun disableTwoFactor(@Body body: Map<String, String>): Response<Unit>

    @POST("auth/2fa/loginVerifyLastStep")
    suspend fun loginVerifyTwoFactorLastStep(@Body body: Map<String, String>): BasicResponse
}