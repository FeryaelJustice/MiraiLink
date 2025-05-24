package com.feryaeljustice.mirailink.data.remote

import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.UserPhotoDto
import com.feryaeljustice.mirailink.data.model.request.LoginRequest
import com.feryaeljustice.mirailink.data.model.request.RegisterRequest
import com.feryaeljustice.mirailink.data.model.response.BasicResponse
import com.feryaeljustice.mirailink.data.model.response.LoginResponse
import com.feryaeljustice.mirailink.data.model.response.LogoutResponse
import com.feryaeljustice.mirailink.data.model.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface UserApiService {
    @POST("testAuth")
    suspend fun testAuth(): BasicResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/logout")
    suspend fun logout(): LogoutResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @GET("user")
    suspend fun getCurrentUser(): UserDto

    @GET("user/photos")
    suspend fun getUserPhotos(@Query("userId") userId: String): List<UserPhotoDto>

    @PUT("user")
    suspend fun updateBio(@Body bio: Map<String, String>)
}