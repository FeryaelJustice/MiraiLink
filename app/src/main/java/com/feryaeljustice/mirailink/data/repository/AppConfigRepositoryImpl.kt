package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.AppConfigRemoteDataSource
import com.feryaeljustice.mirailink.domain.repository.AppConfigRepository

class AppConfigRepositoryImpl(
    private val remote: AppConfigRemoteDataSource,
) : AppConfigRepository {
    override suspend fun getVersion() = remote.getVersion()
}
