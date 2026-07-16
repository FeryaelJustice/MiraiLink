package com.feryaeljustice.mirailink.data.datasource

import com.feryaeljustice.mirailink.data.mappers.toDomain
import com.feryaeljustice.mirailink.data.remote.AppConfigApiService
import com.feryaeljustice.mirailink.data.util.safeApiCall
import com.feryaeljustice.mirailink.domain.model.AppVersionInfo
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class AppConfigRemoteDataSource(
    private val api: AppConfigApiService,
) {
    suspend fun getVersion(): MiraiLinkResult<AppVersionInfo> =
        safeApiCall {
            api.getAndroidAppVersion().toDomain()
        }
}
