package com.feryaeljustice.mirailink.data.datasource

import android.util.Log
import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.request.SwipeRequest
import com.feryaeljustice.mirailink.data.remote.SwipeApiService
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class SwipeRemoteDataSource @Inject constructor(private val api: SwipeApiService) {
    suspend fun getFeed(): MiraiLinkResult<List<UserDto>> = try {
        val result = api.getFeed()
        MiraiLinkResult.Success(result)
    } catch (e: Exception) {
        MiraiLinkResult.Error("Error obteniendo el feed: ${e.message}", e)
    }

    suspend fun likeUser(toUserId: String): MiraiLinkResult<Boolean> = try {
        val result = api.likeUser(SwipeRequest(toUserId))
        Log.d("SwipeRemoteDataSource", "likeUser: $result")
        MiraiLinkResult.Success(result.match)
    } catch (e: Exception) {
        MiraiLinkResult.Error("Error al dar like: ${e.message}", e)
    }

    suspend fun dislikeUser(toUserId: String): MiraiLinkResult<Unit> = try {
        api.dislikeUser(SwipeRequest(toUserId))
        MiraiLinkResult.Success(Unit)
    } catch (e: Exception) {
        MiraiLinkResult.Error("Error al ignorar usuario: ${e.message}", e)
    }
}