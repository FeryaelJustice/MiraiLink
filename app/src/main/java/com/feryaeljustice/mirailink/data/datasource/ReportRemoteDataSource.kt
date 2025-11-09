package com.feryaeljustice.mirailink.data.datasource

import com.feryaeljustice.mirailink.data.model.request.report.ReportUserRequest
import com.feryaeljustice.mirailink.data.remote.ReportApiService
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.domain.util.parseMiraiLinkHttpError

class ReportRemoteDataSource(
    private val api: ReportApiService,
) {
    suspend fun reportUser(
        reportedUser: String,
        reason: String,
    ): MiraiLinkResult<Unit> =
        try {
            val request = ReportUserRequest(reportedUser, reason)
            api.reportUser(request)
            MiraiLinkResult.Success(Unit)
        } catch (e: Throwable) {
            parseMiraiLinkHttpError(e, "ReportRemoteDataSource", "reportUser")
        }
}
