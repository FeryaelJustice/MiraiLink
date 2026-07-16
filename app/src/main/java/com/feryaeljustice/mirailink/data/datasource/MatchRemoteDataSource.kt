package com.feryaeljustice.mirailink.data.datasource

import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.request.match.MarkMatchAsSeenRequest
import com.feryaeljustice.mirailink.data.remote.MatchApiService
import com.feryaeljustice.mirailink.data.util.NetworkOperation
import com.feryaeljustice.mirailink.data.util.safeApiCall
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class MatchRemoteDataSource(
    private val api: MatchApiService,
) {
    suspend fun getMatches(): MiraiLinkResult<List<UserDto>> =
        safeApiCall(NetworkOperation.AUTHENTICATED) {
            api.getMatches()
        }

    suspend fun getUnseenMatches(): MiraiLinkResult<List<UserDto>> =
        safeApiCall(NetworkOperation.AUTHENTICATED) {
            api.getUnseenMatches()
        }

    suspend fun markMatchAsSeen(matchIds: List<String>): MiraiLinkResult<Unit> =
        safeApiCall(NetworkOperation.AUTHENTICATED) {
            api.markMatchAsSeen(MarkMatchAsSeenRequest(matchIds))
        }
}
