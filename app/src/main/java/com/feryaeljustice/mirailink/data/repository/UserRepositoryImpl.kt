package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.UserRemoteDataSource
import com.feryaeljustice.mirailink.data.mappers.toDomain
import com.feryaeljustice.mirailink.domain.model.User
import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.domain.util.resolvePhotoUrls
import javax.inject.Inject
import javax.inject.Named

class UserRepositoryImpl @Inject constructor(
    private val remote: UserRemoteDataSource,
    @Named("BaseUrl") private val baseUrl: String,
) : UserRepository {

    override suspend fun testAuth(): MiraiLinkResult<Unit> = remote.testAuth()

    override suspend fun login(email: String, username: String, password: String): MiraiLinkResult<String> {
        return remote.login(email, username, password)
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

    override suspend fun requestPasswordReset(email: String) = remote.requestPasswordReset(email)
    override suspend fun confirmPasswordReset(email: String, token: String, newPassword: String) =
        remote.confirmPasswordReset(email, token, newPassword)

    override suspend fun requestVerificationCode(userId: String, type: String) =
        remote.requestVerificationCode(userId, type)

    override suspend fun confirmVerificationCode(userId: String, token: String, type: String) =
        remote.confirmVerificationCode(userId, token, type)

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