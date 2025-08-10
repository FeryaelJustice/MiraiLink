package com.feryaeljustice.mirailink.data.datasource

import com.feryaeljustice.mirailink.data.mappers.toDomain
import com.feryaeljustice.mirailink.data.remote.AppConfigApiService
import com.feryaeljustice.mirailink.domain.model.AppVersionInfo
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.domain.util.parseMiraiLinkHttpError
import javax.inject.Inject

class AppConfigRemoteDataSource @Inject constructor(
    private val api: AppConfigApiService
) {
    suspend fun getVersion(): MiraiLinkResult<AppVersionInfo> = try {
        val dto = api.getAndroidAppVersion()
        MiraiLinkResult.success(dto.toDomain())
    } catch (t: Throwable) {
        parseMiraiLinkHttpError(t, "AppConfig", "getVersion")
    }
}