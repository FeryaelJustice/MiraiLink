package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.UsersRemoteDataSource
import com.feryaeljustice.mirailink.data.mappers.toDomain
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.repository.UsersRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.domain.util.resolvePhotoUrls

class UsersRepositoryImpl(
    private val remote: UsersRemoteDataSource,
    private val baseUrl: String,
) : UsersRepository {
    override suspend fun getUsers(): MiraiLinkResult<List<User>> =
        when (val result = remote.getUsers()) {
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
}
