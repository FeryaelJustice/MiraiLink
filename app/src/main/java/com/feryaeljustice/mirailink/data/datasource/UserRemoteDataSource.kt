package com.feryaeljustice.mirailink.data.datasource

import android.content.Context
import android.net.Uri
import android.util.Log
import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.UserPhotoDto
import com.feryaeljustice.mirailink.data.model.request.auth.LoginRequest
import com.feryaeljustice.mirailink.data.model.request.auth.PasswordResetConfirmRequest
import com.feryaeljustice.mirailink.data.model.request.auth.RegisterRequest
import com.feryaeljustice.mirailink.data.model.request.generic.ByIdRequest
import com.feryaeljustice.mirailink.data.model.request.generic.EmailRequest
import com.feryaeljustice.mirailink.data.model.request.verification.VerificationConfirmRequest
import com.feryaeljustice.mirailink.data.model.request.verification.VerificationRequest
import com.feryaeljustice.mirailink.data.remote.UserApiService
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.domain.util.parseMiraiLinkHttpError
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

// For only current user operations (auth and authenticated for own user logged)
class UserRemoteDataSource @Inject constructor(
    private val api: UserApiService,
    private val context: Context
) {
    suspend fun autologin(): MiraiLinkResult<String> {
        return try {
            val res = api.autologin()
            MiraiLinkResult.success(res.userId)
        } catch (e: Throwable) {
            parseMiraiLinkHttpError(e, "UserRemoteDataSource", "autologin")
        }
    }

    suspend fun login(email: String, username: String, password: String): MiraiLinkResult<String> {
        return try {
            val response = api.login(LoginRequest(email, username, password))
            MiraiLinkResult.success(response.token)
        } catch (e: Throwable) {
            parseMiraiLinkHttpError(e, "UserRemoteDataSource", "login")
        }
    }

    suspend fun logout(): MiraiLinkResult<Boolean> {
        return try {
            api.logout()
            MiraiLinkResult.success(true)
        } catch (e: Throwable) {
            parseMiraiLinkHttpError(e, "UserRemoteDataSource", "logout")
        }
    }

    suspend fun register(
        username: String,
        email: String,
        password: String
    ): MiraiLinkResult<String> {
        return try {
            val response = api.register(RegisterRequest(username, email, password))
            MiraiLinkResult.success(response.token)
        } catch (e: Throwable) {
            parseMiraiLinkHttpError(e, "UserRemoteDataSource", "register")
        }
    }

    suspend fun requestPasswordReset(email: String): MiraiLinkResult<String> = try {
        val response = api.requestPasswordReset(EmailRequest(email))
        MiraiLinkResult.Success(response.message)
    } catch (e: Throwable) {
        parseMiraiLinkHttpError(e, "UserRemoteDataSource", "requestPasswordReset")
    }

    suspend fun confirmPasswordReset(
        email: String,
        token: String,
        newPassword: String
    ): MiraiLinkResult<String> = try {
        val response =
            api.confirmPasswordReset(PasswordResetConfirmRequest(email, token, newPassword))
        MiraiLinkResult.Success(response.message)
    } catch (e: Throwable) {
        parseMiraiLinkHttpError(e, "UserRemoteDataSource", "confirmPasswordReset")
    }

    suspend fun checkIsVerified(): MiraiLinkResult<Boolean> = try {
        val response = api.checkIsVerified()
        MiraiLinkResult.Success(response.isVerified)
    } catch (e: Throwable) {
        parseMiraiLinkHttpError(e, "UserRemoteDataSource", "checkIsVerified")
    }

    suspend fun requestVerificationCode(userId: String, type: String): MiraiLinkResult<String> =
        try {
            val response = api.requestVerificationCode(VerificationRequest(userId, type))
            MiraiLinkResult.Success(response.message)
        } catch (e: Throwable) {
            parseMiraiLinkHttpError(e, "UserRemoteDataSource", "requestVerificationCode")
        }

    suspend fun confirmVerificationCode(
        userId: String,
        token: String,
        type: String
    ): MiraiLinkResult<String> = try {
        val response = api.confirmVerificationCode(VerificationConfirmRequest(userId, token, type))
        MiraiLinkResult.Success(response.message)
    } catch (e: Throwable) {
        parseMiraiLinkHttpError(e, "UserRemoteDataSource", "confirmVerificationCode")
    }

    suspend fun getCurrentUser(): MiraiLinkResult<Pair<UserDto, List<UserPhotoDto>>> {
        return try {
            val user = api.getCurrentUser()
            val photos = api.getUserPhotos(userId = user.id)
            MiraiLinkResult.success(user to photos)
        } catch (e: Throwable) {
            parseMiraiLinkHttpError(e, "UserRemoteDataSource", "getCurrentUser")
        }
    }

    suspend fun getUserById(userId: String): MiraiLinkResult<Pair<UserDto, List<UserPhotoDto>>> {
        return try {
            val user = api.getUserById(ByIdRequest(userId))
            val photos = api.getUserPhotos(userId = user.id)
            MiraiLinkResult.success(user to photos)
        } catch (e: Throwable) {
            parseMiraiLinkHttpError(e, "UserRemoteDataSource", "getUserById")
        }
    }

    suspend fun updateProfile(
        nickname: String,
        bio: String,
        animesJson: String,
        gamesJson: String,
        photoUris: List<Uri?>
    ): MiraiLinkResult<Unit> {
        return try {
            val contentResolver = context.contentResolver

            // Convertir URIs a MultipartBody.Part?, si existen
            val photoParts = photoUris.mapIndexed { index, uri ->
                uri?.let {
                    val inputStream = contentResolver.openInputStream(it)
                        ?: return MiraiLinkResult.Error("No se pudo abrir la imagen en slot $index")
                    val mimeType = contentResolver.getType(it) ?: "image/jpeg"
                    val fileName = "photo_${System.currentTimeMillis()}_$index.jpg"
                    val requestBody = inputStream.readBytes()
                        .toRequestBody(mimeType.toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("photo_$index", fileName, requestBody)
                }
            }

            Log.d(
                "UserRemoteDataSource",
                "updateProfile - photoParts: $photoParts | nickname: $nickname | bio: $bio | animesJson: $animesJson | gamesJson: $gamesJson"
            )

            // Llamar a la API
            api.updateProfile(
                nickname = nickname.toRequestBody(),
                bio = bio.toRequestBody(),
                animes = animesJson.toRequestBody(),
                games = gamesJson.toRequestBody(),
//                photos = photosJson.toRequestBody(),
                photo_0 = photoParts.getOrNull(0),
                photo_1 = photoParts.getOrNull(1),
                photo_2 = photoParts.getOrNull(2),
                photo_3 = photoParts.getOrNull(3)
            )

            MiraiLinkResult.Success(Unit)
        } catch (e: Throwable) {
            parseMiraiLinkHttpError(e, "UserRemoteDataSource", "updateProfile")
        }
    }

    suspend fun hasProfilePicture(userId: String): MiraiLinkResult<Boolean> {
        return try {
            val userPhotos = api.getUserPhotos(userId)
            MiraiLinkResult.success(userPhotos.any { it.position == 1 })
        } catch (e: Throwable) {
            parseMiraiLinkHttpError(e, "UserRemoteDataSource", "hasProfilePicture")
        }
    }

    suspend fun uploadUserPhoto(uri: Uri): MiraiLinkResult<String> {
        return try {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(uri)
                ?: return MiraiLinkResult.Error("No se pudo abrir la imagen")

            val mimeType = contentResolver.getType(uri) ?: "image/jpeg"
            val fileName = "photo_${System.currentTimeMillis()}.jpg"

            val requestBody = inputStream.readBytes().toRequestBody(mimeType.toMediaTypeOrNull())
            val multipart = MultipartBody.Part.createFormData("photo", fileName, requestBody)

            val position = "1".toRequestBody("text/plain".toMediaType())
            val response = api.uploadUserPhoto(multipart, position)

            MiraiLinkResult.Success(response.url)
        } catch (e: Throwable) {
            parseMiraiLinkHttpError(e, "UserRemoteDataSource", "uploadUserPhoto")
        }
    }
}