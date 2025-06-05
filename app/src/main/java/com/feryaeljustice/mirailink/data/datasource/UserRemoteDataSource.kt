package com.feryaeljustice.mirailink.data.datasource

import android.content.Context
import android.net.Uri
import android.util.Log
import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.UserPhotoDto
import com.feryaeljustice.mirailink.data.model.request.ByIdRequest
import com.feryaeljustice.mirailink.data.model.request.EmailRequest
import com.feryaeljustice.mirailink.data.model.request.LoginRequest
import com.feryaeljustice.mirailink.data.model.request.PasswordResetConfirmRequest
import com.feryaeljustice.mirailink.data.model.request.RegisterRequest
import com.feryaeljustice.mirailink.data.model.request.VerificationConfirmRequest
import com.feryaeljustice.mirailink.data.model.request.VerificationRequest
import com.feryaeljustice.mirailink.data.remote.UserApiService
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
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
            if (e is HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("UserRemoteDataSource", "autologin - Error HTTP Body: $errorBody")
            } else {
                Log.w("UserRemoteDataSource", "autologin", e)
            }
            MiraiLinkResult.error("autologin error: ${e.message}", e)
        }
    }

    suspend fun login(email: String, username: String, password: String): MiraiLinkResult<String> {
        return try {
            val response = api.login(LoginRequest(email, username, password))
            MiraiLinkResult.success(response.token)
        } catch (e: Throwable) {
            if (e is HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("UserRemoteDataSource", "Login - Error HTTP Body: $errorBody")
            } else {
                Log.w("UserRemoteDataSource", "Login", e)
            }
            MiraiLinkResult.error("Login error: ${e.message}", e)
        }
    }

    suspend fun logout(): MiraiLinkResult<Boolean> {
        return try {
            api.logout()
            MiraiLinkResult.success(true)
        } catch (e: Throwable) {
            if (e is HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("UserRemoteDataSource", "Logout - Error HTTP Body: $errorBody")
            } else {
                Log.w("UserRemoteDataSource", "Logout", e)
            }
            MiraiLinkResult.error("Logout error: ${e.message}", e)
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
            if (e is HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("UserRemoteDataSource", "Login - Error HTTP Body: $errorBody")
            } else {
                Log.w("UserRemoteDataSource", "Login", e)
            }
            MiraiLinkResult.error("Login error: ${e.message}", e)
        }
    }

    suspend fun requestPasswordReset(email: String): MiraiLinkResult<String> = try {
        val response = api.requestPasswordReset(EmailRequest(email))
        MiraiLinkResult.Success(response.message)
    } catch (e: Exception) {
        MiraiLinkResult.Error("Error solicitando recuperación de contraseña: ${e.message}", e)
    }

    suspend fun confirmPasswordReset(
        email: String,
        token: String,
        newPassword: String
    ): MiraiLinkResult<String> = try {
        val response =
            api.confirmPasswordReset(PasswordResetConfirmRequest(email, token, newPassword))
        MiraiLinkResult.Success(response.message)
    } catch (e: Exception) {
        MiraiLinkResult.Error("Error confirmando nueva contraseña: ${e.message}", e)
    }

    suspend fun requestVerificationCode(userId: String, type: String): MiraiLinkResult<String> =
        try {
            val response = api.requestVerificationCode(VerificationRequest(userId, type))
            MiraiLinkResult.Success(response.message)
        } catch (e: Exception) {
            MiraiLinkResult.Error("Error solicitando código de verificación: ${e.message}", e)
        }

    suspend fun confirmVerificationCode(
        userId: String,
        token: String,
        type: String
    ): MiraiLinkResult<String> = try {
        val response = api.confirmVerificationCode(VerificationConfirmRequest(userId, token, type))
        MiraiLinkResult.Success(response.message)
    } catch (e: Exception) {
        MiraiLinkResult.Error("Código incorrecto", e)
    }

    suspend fun getCurrentUser(): MiraiLinkResult<Pair<UserDto, List<UserPhotoDto>>> {
        return try {
            val user = api.getCurrentUser()
            val photos = api.getUserPhotos(userId = user.id)
            MiraiLinkResult.success(user to photos)
        } catch (e: Throwable) {
            if (e is HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("UserRemoteDataSource", "getCurrentUser - Error HTTP Body: $errorBody")
            } else {
                Log.w("UserRemoteDataSource", "getCurrentUser", e)
            }
            MiraiLinkResult.error("No se pudo obtener el usuario actual: ${e.message}", e)
        }
    }

    suspend fun getUserById(userId: String): MiraiLinkResult<Pair<UserDto, List<UserPhotoDto>>> {
        return try {
            val user = api.getUserById(ByIdRequest(userId))
            val photos = api.getUserPhotos(userId = user.id)
            MiraiLinkResult.success(user to photos)
        } catch (e: Throwable) {
            if (e is HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("UserRemoteDataSource", "getUserById - Error HTTP Body: $errorBody")
            } else {
                Log.w("UserRemoteDataSource", "getUserById", e)
            }
            MiraiLinkResult.error("No se pudo obtener el usuario by id: ${e.message}", e)
        }
    }

    suspend fun updateBio(bio: String): MiraiLinkResult<Unit> {
        return try {
            api.updateBio(mapOf("bio" to bio))
            MiraiLinkResult.success(Unit)
        } catch (e: Exception) {
            if (e is HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("UserRemoteDataSource", "updateBio - Error HTTP Body: $errorBody")
            } else {
                Log.w("UserRemoteDataSource", "updateBio", e)
            }
            MiraiLinkResult.error("No se pudo actualizar la biografía: ${e.message}", e)
        }
    }

    suspend fun hasProfilePicture(userId: String): MiraiLinkResult<Boolean> {
        return try {
            val userPhotos = api.getUserPhotos(userId)
            MiraiLinkResult.success(userPhotos.any { it.position == 1 })
        } catch (e: Exception) {
            MiraiLinkResult.error("hasProfilePicture: ${e.message}", e)
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
        } catch (e: Exception) {
            MiraiLinkResult.error("uploadUserPhoto: ${e.message}", e)
        }
    }
}