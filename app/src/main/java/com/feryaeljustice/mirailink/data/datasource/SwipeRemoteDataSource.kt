package com.feryaeljustice.mirailink.data.datasource

import android.util.Log
import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.request.swipe.SwipeRequest
import com.feryaeljustice.mirailink.data.remote.SwipeApiService
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.domain.util.parseMiraiLinkHttpError
import javax.inject.Inject

class SwipeRemoteDataSource @Inject constructor(private val api: SwipeApiService) {
    suspend fun getFeed(): MiraiLinkResult<List<UserDto>> = try {
        val result = api.getFeed()
        MiraiLinkResult.Success(result)
    } catch (e: Throwable) {
        parseMiraiLinkHttpError(e, "SwipeRemoteDataSource", "getFeed")
    }

    suspend fun likeUser(toUserId: String): MiraiLinkResult<Boolean> = try {
        val result = api.likeUser(SwipeRequest(toUserId))
        Log.d("SwipeRemoteDataSource", "likeUser: $result")
        MiraiLinkResult.Success(result.match)
    } catch (e: Throwable) {
        parseMiraiLinkHttpError(e, "SwipeRemoteDataSource", "likeUser")
    }

    suspend fun dislikeUser(toUserId: String): MiraiLinkResult<Unit> = try {
        api.dislikeUser(SwipeRequest(toUserId))
        MiraiLinkResult.Success(Unit)
    } catch (e: Throwable) {
        parseMiraiLinkHttpError(e, "SwipeRemoteDataSource", "dislikeUser")
    }
}