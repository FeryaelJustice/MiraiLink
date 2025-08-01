package com.feryaeljustice.mirailink.domain.usecase.auth.two_factor

import com.feryaeljustice.mirailink.domain.repository.TwoFactorRepository
import javax.inject.Inject

class DisableTwoFactorUseCase @Inject constructor(private val repo: TwoFactorRepository) {
    suspend operator fun invoke() = repo.disable2FA()
}