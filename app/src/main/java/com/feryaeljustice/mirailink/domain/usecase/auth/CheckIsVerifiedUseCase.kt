/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.auth

import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class CheckIsVerifiedUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(): MiraiLinkResult<Boolean> {
        return try {
            repository.checkIsVerified()
        } catch (e: Exception) {
            MiraiLinkResult.Error("An error occurred while checking if the user is verified", e)
        }
    }
}