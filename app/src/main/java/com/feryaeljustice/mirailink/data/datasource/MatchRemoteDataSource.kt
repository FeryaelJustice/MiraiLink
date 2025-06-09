package com.feryaeljustice.mirailink.data.datasource

import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.request.match.MarkMatchAsSeenRequest
import com.feryaeljustice.mirailink.data.remote.MatchApiService
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class MatchRemoteDataSource @Inject constructor(private val api: MatchApiService) {
    suspend fun getMatches(): MiraiLinkResult<List<UserDto>> = try {
        val result = api.getMatches()
        MiraiLinkResult.Success(result)
    } catch (e: Exception) {
        MiraiLinkResult.Error("Error obteniendo el feed de matches: ${e.message}", e)
    }

    suspend fun getUnseenMatches(): MiraiLinkResult<List<UserDto>> = try {
        val result = api.getUnseenMatches()
        MiraiLinkResult.Success(result)
    } catch (e: Exception) {
        MiraiLinkResult.Error("Error obteniendo el feed de matches no vistos: ${e.message}", e)
    }

    suspend fun markMatchAsSeen(matchIds: List<String>): MiraiLinkResult<Unit> = try {
        val result = api.markMatchAsSeen(MarkMatchAsSeenRequest(matchIds))
        MiraiLinkResult.Success(Unit)
    } catch (e: Exception) {
        MiraiLinkResult.Error("Error marcando un match como visto: ${e.message}", e)
    }
}