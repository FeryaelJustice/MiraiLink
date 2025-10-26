package com.feryaeljustice.mirailink.domain.usecase.users

import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): MiraiLinkResult<Unit> = try {
        repository.deleteAccount()
    } catch (e: Exception) {
        MiraiLinkResult.Error("DeleteAccountUseCase error: ", e)
    }
}