package com.feryaeljustice.mirailink.data.remote

import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.request.match.MarkMatchAsSeenRequest
import com.feryaeljustice.mirailink.data.model.response.generic.BasicResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MatchApiService {
    companion object {
        const val MATCH_PREFIX = "match"
    }

    @GET(MATCH_PREFIX)
    suspend fun getMatches(): List<UserDto>

    @GET("$MATCH_PREFIX/unseen")
    suspend fun getUnseenMatches(): List<UserDto>

    @POST("$MATCH_PREFIX/mark-seen")
    suspend fun markMatchAsSeen(@Body request: MarkMatchAsSeenRequest): BasicResponse
}