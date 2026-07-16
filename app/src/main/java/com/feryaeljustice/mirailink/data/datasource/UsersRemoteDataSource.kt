package com.feryaeljustice.mirailink.data.datasource

import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.remote.UsersApiService
import com.feryaeljustice.mirailink.data.util.NetworkOperation
import com.feryaeljustice.mirailink.data.util.safeApiCall
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class UsersRemoteDataSource(
    private val api: UsersApiService,
) {
    suspend fun getUsers(): MiraiLinkResult<List<UserDto>> =
        safeApiCall(NetworkOperation.AUTHENTICATED) {
            api.getUsers()
        }
}
