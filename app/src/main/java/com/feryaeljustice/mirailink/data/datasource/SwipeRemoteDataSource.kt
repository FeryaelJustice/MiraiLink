package com.feryaeljustice.mirailink.data.datasource

import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.request.swipe.SwipeRequest
import com.feryaeljustice.mirailink.data.remote.SwipeApiService
import com.feryaeljustice.mirailink.data.util.NetworkOperation
import com.feryaeljustice.mirailink.data.util.safeApiCall
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class SwipeRemoteDataSource(
    private val api: SwipeApiService,
) {
    suspend fun getFeed(): MiraiLinkResult<List<UserDto>> =
        safeApiCall(NetworkOperation.AUTHENTICATED) {
            api.getFeed()
        }

    suspend fun likeUser(toUserId: String): MiraiLinkResult<Boolean> =
        safeApiCall(NetworkOperation.AUTHENTICATED) {
            api.likeUser(SwipeRequest(toUserId)).match
        }

    suspend fun dislikeUser(toUserId: String): MiraiLinkResult<Unit> =
        safeApiCall(NetworkOperation.AUTHENTICATED) {
            api.dislikeUser(SwipeRequest(toUserId))
        }
}
