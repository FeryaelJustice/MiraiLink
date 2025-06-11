package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.ReportRemoteDataSource
import com.feryaeljustice.mirailink.domain.repository.ReportRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val remote: ReportRemoteDataSource
) : ReportRepository {

    override suspend fun reportUser(
        reportedUser: String,
        reason: String
    ): MiraiLinkResult<Unit> {
        return when (val result = remote.reportUser(reportedUser, reason)) {
            is MiraiLinkResult.Success -> {
                MiraiLinkResult.Success(Unit)
            }

            is MiraiLinkResult.Error -> result
        }
    }

}