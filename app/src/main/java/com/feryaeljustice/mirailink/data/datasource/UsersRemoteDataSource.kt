package com.feryaeljustice.mirailink.data.datasource

import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.remote.UsersApiService
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.domain.util.parseMiraiLinkHttpError
import javax.inject.Inject

class UsersRemoteDataSource @Inject constructor(
    private val api: UsersApiService
) {
    suspend fun getUsers(): MiraiLinkResult<List<UserDto>> {
        return try {
            MiraiLinkResult.success(api.getUsers())
        } catch (e: Throwable) {
            parseMiraiLinkHttpError(e, "UsersRemoteDataSource", "getUsers")
        }
    }
}