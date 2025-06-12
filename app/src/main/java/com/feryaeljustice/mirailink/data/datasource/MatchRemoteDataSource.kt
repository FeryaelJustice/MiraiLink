package com.feryaeljustice.mirailink.data.datasource

import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.request.match.MarkMatchAsSeenRequest
import com.feryaeljustice.mirailink.data.remote.MatchApiService
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.domain.util.parseMiraiLinkHttpError
import javax.inject.Inject

class MatchRemoteDataSource @Inject constructor(private val api: MatchApiService) {
    suspend fun getMatches(): MiraiLinkResult<List<UserDto>> = try {
        val result = api.getMatches()
        MiraiLinkResult.Success(result)
    } catch (e: Throwable) {
        parseMiraiLinkHttpError(e, "MatchRemoteDataSource", "getMatches")
    }

    suspend fun getUnseenMatches(): MiraiLinkResult<List<UserDto>> = try {
        val result = api.getUnseenMatches()
        MiraiLinkResult.Success(result)
    } catch (e: Throwable) {
        parseMiraiLinkHttpError(e, "MatchRemoteDataSource", "getUnseenMatches")
    }

    suspend fun markMatchAsSeen(matchIds: List<String>): MiraiLinkResult<Unit> = try {
        api.markMatchAsSeen(MarkMatchAsSeenRequest(matchIds))
        MiraiLinkResult.Success(Unit)
    } catch (e: Throwable) {
        parseMiraiLinkHttpError(e, "MatchRemoteDataSource", "markMatchAsSeen")
    }
}