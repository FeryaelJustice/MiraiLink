/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.auth

import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class AutologinUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): MiraiLinkResult<String> {
        return try {
            repository.autologin()
        } catch (e: Exception) {
            MiraiLinkResult.Error("AutologinUseCase error: ", e)
        }
    }
}