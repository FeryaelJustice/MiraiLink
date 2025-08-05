package com.feryaeljustice.mirailink.domain.usecase.auth.two_factor

import com.feryaeljustice.mirailink.domain.repository.TwoFactorRepository
import javax.inject.Inject

class LoginVerifyTwoFactorLastStepUseCase @Inject constructor(private val repo: TwoFactorRepository) {
    suspend operator fun invoke(userId: String, code: String) =
        repo.loginVerifyTwoFactorLastStep(userId = userId, code = code)
}