package com.feryaeljustice.mirailink.data.mappers

import com.feryaeljustice.mirailink.data.model.AppVersionInfoDto
import com.feryaeljustice.mirailink.domain.model.AppVersionInfo

fun AppVersionInfoDto.toDomain(): AppVersionInfo = AppVersionInfo(
    platform = platform,
    minVersionCode = minVersionCode,
    latestVersionCode = latestVersionCode,
    message = message,
    playStoreUrl = playStoreUrl
)