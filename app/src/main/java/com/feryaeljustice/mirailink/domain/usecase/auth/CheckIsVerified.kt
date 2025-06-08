package com.feryaeljustice.mirailink.domain.usecase.auth

import android.util.Log
import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class CheckIsVerified @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(): MiraiLinkResult<Boolean> {
        Log.d("CheckIsVerified", "invoke: Checking if user is verified")
        return repository.checkIsVerified()
    }
}