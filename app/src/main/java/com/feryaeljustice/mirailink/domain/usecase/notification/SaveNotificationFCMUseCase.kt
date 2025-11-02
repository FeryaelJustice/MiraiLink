package com.feryaeljustice.mirailink.domain.usecase.notification

import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

/**
 * @author Feryael Justice
 * @since 26/10/2024
 */

class SaveNotificationFCMUseCase
    @Inject
    constructor(
        private val repository: UserRepository,
    ) {
        suspend operator fun invoke(fcm: String): MiraiLinkResult<Unit> =
            try {
                repository.saveUserFCM(fcm = fcm)
            } catch (e: Exception) {
                MiraiLinkResult.Error("SaveNotificationFCMUseCase error: ", e)
            }
    }
