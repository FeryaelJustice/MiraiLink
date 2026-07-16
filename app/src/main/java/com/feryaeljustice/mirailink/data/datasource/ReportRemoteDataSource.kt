package com.feryaeljustice.mirailink.data.datasource

import com.feryaeljustice.mirailink.data.model.request.report.ReportUserRequest
import com.feryaeljustice.mirailink.data.remote.ReportApiService
import com.feryaeljustice.mirailink.data.util.NetworkOperation
import com.feryaeljustice.mirailink.data.util.safeApiUnitResponse
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class ReportRemoteDataSource(
    private val api: ReportApiService,
) {
    suspend fun reportUser(
        reportedUser: String,
        reason: String,
    ): MiraiLinkResult<Unit> =
        safeApiUnitResponse(NetworkOperation.AUTHENTICATED) {
            api.reportUser(ReportUserRequest(reportedUser, reason))
        }
}
