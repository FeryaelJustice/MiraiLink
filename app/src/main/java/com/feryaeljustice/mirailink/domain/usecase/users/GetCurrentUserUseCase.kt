package com.feryaeljustice.mirailink.domain.usecase.users

import android.util.Log
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): MiraiLinkResult<User> {
        val user = repository.getCurrentUser()
        Log.d("GetCurrentUserUseCase", "User: $user")
        return user
    }
}