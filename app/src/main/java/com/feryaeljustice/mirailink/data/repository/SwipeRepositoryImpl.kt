package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.SwipeRemoteDataSource
import com.feryaeljustice.mirailink.data.mappers.toDomain
import com.feryaeljustice.mirailink.domain.error.ValidationError
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.repository.SwipeRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.domain.util.resolvePhotoUrls
import java.util.UUID

class SwipeRepositoryImpl(
    private val remote: SwipeRemoteDataSource,
    private val baseUrl: String,
) : SwipeRepository {
    override suspend fun getFeed(): MiraiLinkResult<List<User>> =
        when (val result = remote.getFeed()) {
            is MiraiLinkResult.Success -> {
                val users =
                    result.data.filter { it.id.isCanonicalUuid() }.map { userDto ->
                        val user = userDto.toDomain()

                        val orderedPhotos = resolvePhotoUrls(baseUrl, user.photos)
                        user.copy(photos = orderedPhotos)
                    }
                MiraiLinkResult.Success(users)
            }

            is MiraiLinkResult.Error -> result
        }

    override suspend fun likeUser(toUserId: String): MiraiLinkResult<Boolean> =
        if (toUserId.isCanonicalUuid()) remote.likeUser(toUserId)
        else MiraiLinkResult.Error(ValidationError.INVALID_INPUT)

    override suspend fun dislikeUser(toUserId: String): MiraiLinkResult<Unit> =
        if (toUserId.isCanonicalUuid()) remote.dislikeUser(toUserId)
        else MiraiLinkResult.Error(ValidationError.INVALID_INPUT)
}

private fun String.isCanonicalUuid(): Boolean =
    runCatching { UUID.fromString(this) }
        .getOrNull()
        ?.toString()
        ?.equals(this, ignoreCase = true) == true
