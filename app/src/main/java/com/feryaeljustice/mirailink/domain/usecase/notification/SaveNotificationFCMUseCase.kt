package com.feryaeljustice.mirailink.domain.usecase.notification

import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

/**
 * @author Feryael Justice
 * @since 26/10/2024
 */

class SaveNotificationFCMUseCase(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(fcm: String): MiraiLinkResult<Unit> =
        repository.saveUserFCM(fcm = fcm)
}
