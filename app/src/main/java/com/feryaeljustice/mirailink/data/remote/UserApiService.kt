package com.feryaeljustice.mirailink.data.remote

import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.UserPhotoDto
import com.feryaeljustice.mirailink.data.model.request.auth.LoginRequest
import com.feryaeljustice.mirailink.data.model.request.auth.PasswordResetConfirmRequest
import com.feryaeljustice.mirailink.data.model.request.auth.RegisterRequest
import com.feryaeljustice.mirailink.data.model.request.generic.ByIdRequest
import com.feryaeljustice.mirailink.data.model.request.generic.EmailRequest
import com.feryaeljustice.mirailink.data.model.request.notifications.SaveFCMUserRequest
import com.feryaeljustice.mirailink.data.model.request.verification.VerificationConfirmRequest
import com.feryaeljustice.mirailink.data.model.request.verification.VerificationRequest
import com.feryaeljustice.mirailink.data.model.response.auth.AutologinResponse
import com.feryaeljustice.mirailink.data.model.response.auth.CheckIsVerifiedResponse
import com.feryaeljustice.mirailink.data.model.response.auth.LoginResponse
import com.feryaeljustice.mirailink.data.model.response.auth.LogoutResponse
import com.feryaeljustice.mirailink.data.model.response.auth.RegisterResponse
import com.feryaeljustice.mirailink.data.model.response.generic.BasicResponse
import com.feryaeljustice.mirailink.data.model.response.photo.UploadPhotoResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApiService {
    @POST("auth/autologin")
    suspend fun autologin(): AutologinResponse

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest,
    ): LoginResponse

    @POST("auth/logout")
    suspend fun logout(): LogoutResponse

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest,
    ): RegisterResponse

    @POST("auth/password/request-reset")
    suspend fun requestPasswordReset(
        @Body body: EmailRequest,
    ): BasicResponse

    @POST("auth/password/confirm-reset")
    suspend fun confirmPasswordReset(
        @Body body: PasswordResetConfirmRequest,
    ): BasicResponse

    @GET("auth/verification/check")
    suspend fun checkIsVerified(): CheckIsVerifiedResponse

    @POST("auth/verification/request")
    suspend fun requestVerificationCode(
        @Body body: VerificationRequest,
    ): BasicResponse

    @POST("auth/verification/confirm")
    suspend fun confirmVerificationCode(
        @Body body: VerificationConfirmRequest,
    ): BasicResponse

    @GET("user")
    suspend fun getCurrentUser(): UserDto

    @DELETE("user")
    suspend fun deleteAccount(): BasicResponse

    @DELETE("user/photo/{position}")
    suspend fun deleteUserPhoto(
        @Path("position") position: Int,
    ): Response<Unit>

    @GET("user/photos")
    suspend fun getUserPhotos(
        @Query("userId") userId: String,
    ): List<UserPhotoDto>

    @Multipart
    @POST("user/photos")
    suspend fun uploadUserPhoto(
        @Part photo: MultipartBody.Part,
        @Part("position") position: RequestBody,
    ): UploadPhotoResponse

    @POST("user/byId")
    suspend fun getUserById(
        @Body request: ByIdRequest,
    ): UserDto

    @Multipart
    @PUT("user")
    suspend fun updateProfile(
        @Part("nickname") nickname: RequestBody,
        @Part("bio") bio: RequestBody,
        @Part("gender") gender: RequestBody?,
        @Part("birthdate") birthdate: RequestBody?,
        @Part("animes") animes: RequestBody,
        @Part("games") games: RequestBody,
        @Part("reorderedPositions") reorderedPositions: RequestBody?,
        @Part photo_0: MultipartBody.Part? = null, // Solo si existe nuevo archivo
        @Part photo_1: MultipartBody.Part? = null,
        @Part photo_2: MultipartBody.Part? = null,
        @Part photo_3: MultipartBody.Part? = null,
    ): Response<Unit>

    @POST("user/fcm")
    suspend fun saveUserFcm(
        @Body body: SaveFCMUserRequest,
    ): BasicResponse
}
