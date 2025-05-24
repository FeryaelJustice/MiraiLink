package com.feryaeljustice.mirailink.data.remote

import com.feryaeljustice.mirailink.data.model.UserDto
import retrofit2.http.GET

interface UsersApiService {
    @GET("users")
    suspend fun getUsers(): List<UserDto>
}