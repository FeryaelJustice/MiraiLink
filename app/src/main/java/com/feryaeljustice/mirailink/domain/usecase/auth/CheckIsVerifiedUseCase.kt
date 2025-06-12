package com.feryaeljustice.mirailink.domain.usecase.auth

import android.util.Log
import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class CheckIsVerifiedUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(): MiraiLinkResult<Boolean> {
        Log.d("CheckIsVerifiedUseCase", "invoke: Checking if user is verified")
        return repository.checkIsVerified()
    }
}