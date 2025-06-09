package com.feryaeljustice.mirailink.domain.usecase.users

import android.util.Log
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userId: String): MiraiLinkResult<User> {
        val user = repository.getUserById(userId = userId)
        Log.d("GetUserByIdUseCase", "User: $user")
        return user
    }
}