package com.feryaeljustice.mirailink.domain.usecase.users

import android.util.Log
import com.feryaeljustice.mirailink.domain.model.User
import com.feryaeljustice.mirailink.domain.repository.UsersRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: UsersRepository
) {
    suspend operator fun invoke(): MiraiLinkResult<List<User>> {
        val users = repository.getUsers()
        Log.d("GetUsersUseCase", "Users: $users")
        return users
    }
}