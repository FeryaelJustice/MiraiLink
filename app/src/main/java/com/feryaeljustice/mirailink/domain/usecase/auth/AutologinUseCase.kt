/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.auth

import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class AutologinUseCase(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(): MiraiLinkResult<String> =
        try {
            repository.autologin()
        } catch (e: Exception) {
            MiraiLinkResult.Error("AutologinUseCase error: ", e)
        }
}
