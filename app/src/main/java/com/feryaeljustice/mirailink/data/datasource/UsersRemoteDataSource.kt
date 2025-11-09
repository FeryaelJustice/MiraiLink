package com.feryaeljustice.mirailink.data.datasource

import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.remote.UsersApiService
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.domain.util.parseMiraiLinkHttpError

class UsersRemoteDataSource(
    private val api: UsersApiService,
) {
    suspend fun getUsers(): MiraiLinkResult<List<UserDto>> =
        try {
            MiraiLinkResult.success(api.getUsers())
        } catch (e: Throwable) {
            parseMiraiLinkHttpError(e, "UsersRemoteDataSource", "getUsers")
        }
}
