package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.MatchRemoteDataSource
import com.feryaeljustice.mirailink.data.mappers.toDomain
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.repository.MatchRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.domain.util.resolvePhotoUrls

class MatchRepositoryImpl(
    private val remote: MatchRemoteDataSource,
    private val baseUrl: String,
) : MatchRepository {
    override suspend fun getMatches(): MiraiLinkResult<List<User>> =
        when (val result = remote.getMatches()) {
            is MiraiLinkResult.Success -> {
                val users =
                    result.data.map { userDto ->
                        val user = userDto.toDomain()

                        val orderedPhotos = resolvePhotoUrls(baseUrl, user.photos)
                        user.copy(photos = orderedPhotos)
                    }
                MiraiLinkResult.Success(users)
            }

            is MiraiLinkResult.Error -> result
        }

    override suspend fun getUnseenMatches(): MiraiLinkResult<List<User>> =
        when (val result = remote.getUnseenMatches()) {
            is MiraiLinkResult.Success -> {
                val users =
                    result.data.map { userDto ->
                        val user = userDto.toDomain()

                        val orderedPhotos = resolvePhotoUrls(baseUrl, user.photos)
                        user.copy(photos = orderedPhotos)
                    }
                MiraiLinkResult.Success(users)
            }

            is MiraiLinkResult.Error -> result
        }

    override suspend fun markMatchAsSeen(matchIds: List<String>): MiraiLinkResult<Unit> = remote.markMatchAsSeen(matchIds)
}
