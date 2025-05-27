package com.feryaeljustice.mirailink.data.datasource

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
import retrofit2.HttpException
import javax.inject.Inject

// For only current user operations (auth and authenticated for own user logged)
class UserRemoteDataSource @Inject constructor(
    private val api: UserApiService
) {
    suspend fun testAuth(): MiraiLinkResult<Unit> {
        return try {
            api.testAuth()
            MiraiLinkResult.success(Unit)
        } catch (e: Throwable) {
            if (e is HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("UserRemoteDataSource", "testAuth - Error HTTP Body: $errorBody")
            } else {
                Log.w("UserRemoteDataSource", "testAuth", e)
            }
            MiraiLinkResult.error("Login error: ", e)
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
            MiraiLinkResult.error("Login error: ", e)
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
            MiraiLinkResult.error("Logout error: ", e)
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
            MiraiLinkResult.error("Login error: ", e)
        }
    }

    suspend fun requestPasswordReset(email: String): MiraiLinkResult<String> = try {
        val response = api.requestPasswordReset(EmailRequest(email))
        MiraiLinkResult.Success(response.message)
    } catch (e: Exception) {
        MiraiLinkResult.Error("Error solicitando recuperación", e)
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
        MiraiLinkResult.Error("Error confirmando nueva contraseña", e)
    }

    suspend fun requestVerificationCode(userId: String, type: String): MiraiLinkResult<String> =
        try {
            val response = api.requestVerificationCode(VerificationRequest(userId, type))
            MiraiLinkResult.Success(response.message)
        } catch (e: Exception) {
            MiraiLinkResult.Error("Error solicitando código de verificación", e)
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
            MiraiLinkResult.error("No se pudo obtener el usuario actual: ", e)
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
            MiraiLinkResult.error("No se pudo obtener el usuario by id: ", e)
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
            MiraiLinkResult.error("No se pudo actualizar la biografía: ", e)
        }
    }
}