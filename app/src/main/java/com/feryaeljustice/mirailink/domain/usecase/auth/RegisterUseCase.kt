package com.feryaeljustice.mirailink.domain.usecase.auth

import com.feryaeljustice.mirailink.domain.error.ValidationError
import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.domain.util.isAtLeast16YearsOld

class RegisterUseCase(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(
        username: String,
        email: String,
        password: String,
        gender: String,
        birthdate: String,
    ): MiraiLinkResult<String> {
        if (!isAtLeast16YearsOld(birthdate)) {
            return MiraiLinkResult.Error(ValidationError.UNDERAGE)
        }
        return repository.register(username, email, password, gender, birthdate)
    }
}
