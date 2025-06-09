package com.feryaeljustice.mirailink.data.repository

import android.net.Uri
import com.feryaeljustice.mirailink.data.datasource.UserRemoteDataSource
import com.feryaeljustice.mirailink.domain.mappers.toDomain
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.domain.util.resolvePhotoUrls
import javax.inject.Inject
import javax.inject.Named

class UserRepositoryImpl @Inject constructor(
    private val remote: UserRemoteDataSource,
    @Named("BaseUrl") private val baseUrl: String,
) : UserRepository {

    override suspend fun autologin(): MiraiLinkResult<String> = remote.autologin()

    override suspend fun login(
        email: String,
        username: String,
        password: String
    ): MiraiLinkResult<String> {
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

    override suspend fun checkIsVerified() = remote.checkIsVerified()

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

    override suspend fun updateProfile(
        nickname: String,
        bio: String,
        animesJson: String,
        gamesJson: String,
        photosJson: String,
        photoUris: List<Uri?>
    ): MiraiLinkResult<Unit> {
        return remote.updateProfile(
            nickname = nickname,
            bio = bio,
            animesJson = animesJson,
            gamesJson = gamesJson,
            photosJson = photosJson,
            photoUris = photoUris
        )
    }

    override suspend fun hasProfilePicture(userId: String): MiraiLinkResult<Boolean> =
        remote.hasProfilePicture(userId)

    override suspend fun uploadUserPhoto(photo: Uri): MiraiLinkResult<String> =
        remote.uploadUserPhoto(photo)
}