package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.UserRemoteDataSource
import com.feryaeljustice.mirailink.data.mappers.toDomain
import com.feryaeljustice.mirailink.domain.constants.HTTP_REGEX
import com.feryaeljustice.mirailink.domain.model.User
import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.domain.util.resolvePhotoUrls
import javax.inject.Inject
import javax.inject.Named
import kotlin.text.toRegex

class UserRepositoryImpl @Inject constructor(
    private val remote: UserRemoteDataSource,
    @Named("BaseUrl") private val baseUrl: String,
) : UserRepository {

    override suspend fun testAuth(): MiraiLinkResult<Unit> = remote.testAuth()

    override suspend fun login(usernameOrEmail: String, password: String): MiraiLinkResult<String> {
        return remote.login(usernameOrEmail, password)
    }

    override suspend fun logout(): MiraiLinkResult<Boolean> {
        return remote.logout()
    }

    override suspend fun register(
        username: String,
        email: String,
        password: String
    ): MiraiLinkResult<String> {
        return remote.register(username, email, password)
    }

    override suspend fun getCurrentUser(): MiraiLinkResult<User> {
        return when (val result = remote.getCurrentUser()) {
            is MiraiLinkResult.Success -> {
                val (userDto, photos) = result.data
                val domainUserPhotos = photos.map { it.toDomain() }
                val orderedPhotos = resolvePhotoUrls(baseUrl, domainUserPhotos)
                val user = userDto.toDomain().copy(photos = orderedPhotos)
                MiraiLinkResult.Success(user)
            }

            is MiraiLinkResult.Error -> result
        }
    }

    override suspend fun getUserById(userId: String): MiraiLinkResult<User> {
        return when (val result = remote.getUserById(userId)) {
            is MiraiLinkResult.Success -> {
                val (userDto, photos) = result.data
                val domainUserPhotos = photos.map { it.toDomain() }
                val orderedPhotos = resolvePhotoUrls(baseUrl, domainUserPhotos)
                val user = userDto.toDomain().copy(photos = orderedPhotos)
                MiraiLinkResult.Success(user)
            }

            is MiraiLinkResult.Error -> result
        }
    }

    override suspend fun updateBio(bio: String): MiraiLinkResult<Unit> = remote.updateBio(bio)
}