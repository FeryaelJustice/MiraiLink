package com.feryaeljustice.mirailink.data.datasource

import android.util.Log
import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.UserPhotoDto
import com.feryaeljustice.mirailink.data.model.request.ByIdRequest
import com.feryaeljustice.mirailink.data.model.request.LoginRequest
import com.feryaeljustice.mirailink.data.model.request.RegisterRequest
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

    suspend fun login(usernameOrEmail: String, password: String): MiraiLinkResult<String> {
        return try {
            val response = api.login(LoginRequest(usernameOrEmail, password))
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