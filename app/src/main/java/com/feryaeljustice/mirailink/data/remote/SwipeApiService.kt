package com.feryaeljustice.mirailink.data.remote

import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.request.SwipeRequest
import com.feryaeljustice.mirailink.data.model.response.BasicResponse
import com.feryaeljustice.mirailink.data.model.response.SwipeResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SwipeApiService {
    @GET("swipe/feed")
    suspend fun getFeed(@Query("limit") limit: Int = 10, @Query("offset") offset: Int = 0): List<UserDto>

    @POST("swipe/like")
    suspend fun likeUser(@Body request: SwipeRequest): SwipeResponse

    @POST("swipe/dislike")
    suspend fun dislikeUser(@Body request: SwipeRequest): BasicResponse
}