package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.SwipeRemoteDataSource
import com.feryaeljustice.mirailink.data.mappers.toDomain
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.repository.SwipeRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.domain.util.resolvePhotoUrls
import javax.inject.Inject
import javax.inject.Named

class SwipeRepositoryImpl @Inject constructor(
    private val remote: SwipeRemoteDataSource,
    @param:Named("BaseUrl") private val baseUrl: String,
) :
    SwipeRepository {
    override suspend fun getFeed(): MiraiLinkResult<List<User>> {
        return when (val result = remote.getFeed()) {
            is MiraiLinkResult.Success -> {
                val users = result.data.map { userDto ->
                    val user = userDto.toDomain()

                    val orderedPhotos = resolvePhotoUrls(baseUrl, user.photos)
                    user.copy(photos = orderedPhotos)
                }
                MiraiLinkResult.Success(users)
            }

            is MiraiLinkResult.Error -> result
        }
    }

    override suspend fun likeUser(toUserId: String): MiraiLinkResult<Boolean> =
        remote.likeUser(toUserId)

    override suspend fun dislikeUser(toUserId: String): MiraiLinkResult<Unit> =
        remote.dislikeUser(toUserId)
}