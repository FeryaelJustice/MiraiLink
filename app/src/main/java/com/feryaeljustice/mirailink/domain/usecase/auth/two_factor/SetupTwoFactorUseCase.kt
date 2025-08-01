package com.feryaeljustice.mirailink.domain.usecase.auth.two_factor

import com.feryaeljustice.mirailink.domain.repository.TwoFactorRepository
import javax.inject.Inject

class SetupTwoFactorUseCase @Inject constructor(private val repo: TwoFactorRepository) {
    suspend operator fun invoke() = repo.setup2FA()
}