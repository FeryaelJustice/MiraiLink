package com.feryaeljustice.mirailink.domain.repository

import com.feryaeljustice.mirailink.domain.model.AppVersionInfo
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

interface AppConfigRepository {
    suspend fun getVersion(): MiraiLinkResult<AppVersionInfo>
}