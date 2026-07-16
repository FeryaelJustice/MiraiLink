package com.feryaeljustice.mirailink.data.datasource

import android.content.Context
import android.net.Uri
import com.feryaeljustice.mirailink.data.model.ReorderedPhotoDto
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
import com.feryaeljustice.mirailink.data.remote.UserApiService
import com.feryaeljustice.mirailink.data.util.InvalidMediaException
import com.feryaeljustice.mirailink.data.util.NetworkOperation
import com.feryaeljustice.mirailink.data.util.safeApiCall
import com.feryaeljustice.mirailink.data.util.safeApiUnitResponse
import com.feryaeljustice.mirailink.data.util.safeLocalCall
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class UserRemoteDataSource(
    private val api: UserApiService,
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    suspend fun autologin(): MiraiLinkResult<String> =
        safeApiCall(NetworkOperation.AUTHENTICATED) {
            api.autologin().userId
        }

    suspend fun login(
        email: String,
        username: String,
        password: String,
    ): MiraiLinkResult<String> =
        safeApiCall(NetworkOperation.LOGIN) {
            api.login(LoginRequest(email, username, password)).token
        }

    suspend fun logout(): MiraiLinkResult<Boolean> =
        safeApiCall(NetworkOperation.AUTHENTICATED) {
            api.logout()
            true
        }

    suspend fun register(
        username: String,
        email: String,
        password: String,
    ): MiraiLinkResult<String> =
        safeApiCall(NetworkOperation.REGISTER) {
            api.register(RegisterRequest(username, email, password)).token
        }

    suspend fun deleteAccount(): MiraiLinkResult<Unit> =
        safeApiCall(NetworkOperation.AUTHENTICATED) {
            api.deleteAccount()
        }

    suspend fun deleteUserPhoto(position: Int): MiraiLinkResult<Unit> =
        safeApiCall(NetworkOperation.AUTHENTICATED) {
            api.deleteUserPhoto(position)
        }

    suspend fun requestPasswordReset(email: String): MiraiLinkResult<Unit> =
        safeApiCall {
            api.requestPasswordReset(EmailRequest(email))
        }

    suspend fun confirmPasswordReset(
        email: String,
        token: String,
        newPassword: String,
    ): MiraiLinkResult<Unit> =
        safeApiCall(NetworkOperation.VERIFICATION) {
            api.confirmPasswordReset(
                PasswordResetConfirmRequest(email, token, newPassword),
            )
        }

    suspend fun checkIsVerified(): MiraiLinkResult<Boolean> =
        safeApiCall(NetworkOperation.AUTHENTICATED) {
            api.checkIsVerified().isVerified
        }

    suspend fun requestVerificationCode(
        userId: String,
        type: String,
    ): MiraiLinkResult<Unit> =
        safeApiCall(NetworkOperation.VERIFICATION) {
            api.requestVerificationCode(VerificationRequest(userId, type))
        }

    suspend fun confirmVerificationCode(
        userId: String,
        token: String,
        type: String,
    ): MiraiLinkResult<Unit> =
        safeApiCall(NetworkOperation.VERIFICATION) {
            api.confirmVerificationCode(
                VerificationConfirmRequest(userId, token, type),
            )
        }

    suspend fun getCurrentUser(): MiraiLinkResult<Pair<UserDto, List<UserPhotoDto>>> =
        safeApiCall(NetworkOperation.AUTHENTICATED) {
            val user = api.getCurrentUser()
            user to api.getUserPhotos(userId = user.id)
        }

    suspend fun getUserById(userId: String): MiraiLinkResult<Pair<UserDto, List<UserPhotoDto>>> =
        safeApiCall(NetworkOperation.AUTHENTICATED) {
            val user = api.getUserById(ByIdRequest(userId))
            user to api.getUserPhotos(userId = user.id)
        }

    suspend fun updateProfile(
        nickname: String,
        bio: String,
        gender: String?,
        birthdate: String?,
        animesJson: String,
        gamesJson: String,
        photoUris: List<Uri?>,
        existingPhotoUrls: List<String?>,
    ): MiraiLinkResult<Unit> {
        val prepared =
            safeLocalCall(ioDispatcher) {
                val photoParts =
                    photoUris.mapIndexed { index, uri ->
                        uri?.let { createPhotoPart(it, index) }
                    }
                val reordered =
                    existingPhotoUrls.mapIndexedNotNull { index, url ->
                        url?.let { ReorderedPhotoDto(it, index + 1) }
                    }
                PreparedProfileRequest(
                    photoParts = photoParts,
                    reorderedPositions =
                        Json
                            .encodeToString(reordered)
                            .toRequestBody("application/json".toMediaType()),
                )
            }

        return when (prepared) {
            is MiraiLinkResult.Error -> prepared
            is MiraiLinkResult.Success ->
                safeApiUnitResponse(NetworkOperation.AUTHENTICATED) {
                    api.updateProfile(
                        nickname = nickname.toRequestBody(),
                        bio = bio.toRequestBody(),
                        gender = gender?.toRequestBody(),
                        birthdate = birthdate?.toRequestBody(),
                        animes = animesJson.toRequestBody(),
                        games = gamesJson.toRequestBody(),
                        reorderedPositions = prepared.data.reorderedPositions,
                        photo_0 = prepared.data.photoParts.getOrNull(0),
                        photo_1 = prepared.data.photoParts.getOrNull(1),
                        photo_2 = prepared.data.photoParts.getOrNull(2),
                        photo_3 = prepared.data.photoParts.getOrNull(3),
                    )
                }
        }
    }

    suspend fun hasProfilePicture(userId: String): MiraiLinkResult<Boolean> =
        safeApiCall(NetworkOperation.AUTHENTICATED) {
            api.getUserPhotos(userId).any { it.position == 1 }
        }

    suspend fun uploadUserPhoto(uri: Uri): MiraiLinkResult<String> {
        val prepared =
            safeLocalCall(ioDispatcher) {
                val multipart = createPhotoPart(uri, index = 0)
                PreparedPhotoUpload(
                    multipart = multipart,
                    position = "1".toRequestBody("text/plain".toMediaType()),
                )
            }

        return when (prepared) {
            is MiraiLinkResult.Error -> prepared
            is MiraiLinkResult.Success ->
                safeApiCall(NetworkOperation.AUTHENTICATED) {
                    api.uploadUserPhoto(
                        prepared.data.multipart,
                        prepared.data.position,
                    ).url
                }
        }
    }

    suspend fun saveUserFCM(fcm: String): MiraiLinkResult<Unit> =
        safeApiCall(NetworkOperation.AUTHENTICATED) {
            api.saveUserFcm(body = SaveFCMUserRequest(fcm = fcm))
        }

    private fun createPhotoPart(
        uri: Uri,
        index: Int,
    ): MultipartBody.Part {
        val resolver = context.contentResolver
        val bytes =
            resolver.openInputStream(uri)?.use { it.readBytes() }
                ?: throw InvalidMediaException()
        val mimeType = resolver.getType(uri) ?: "image/jpeg"
        val fileName = "photo_${System.currentTimeMillis()}_$index.jpg"
        val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("photo_$index", fileName, requestBody)
    }

    private data class PreparedProfileRequest(
        val photoParts: List<MultipartBody.Part?>,
        val reorderedPositions: RequestBody,
    )

    private data class PreparedPhotoUpload(
        val multipart: MultipartBody.Part,
        val position: RequestBody,
    )
}
