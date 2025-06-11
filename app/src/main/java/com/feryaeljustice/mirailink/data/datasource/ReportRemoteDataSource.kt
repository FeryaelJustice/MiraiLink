package com.feryaeljustice.mirailink.data.datasource

import android.util.Log
import com.feryaeljustice.mirailink.data.model.request.report.ReportUserRequest
import com.feryaeljustice.mirailink.data.remote.ReportApiService
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class ReportRemoteDataSource @Inject constructor(private val api: ReportApiService) {
    suspend fun reportUser(reportedUser: String, reason: String): MiraiLinkResult<Unit> {
        return try {
            val request = ReportUserRequest(reportedUser, reason)
            api.reportUser(request)
            MiraiLinkResult.Success(Unit)
        } catch (e: Exception) {
            Log.e("ReportRemoteDataSource", "reportUser error", e)
            MiraiLinkResult.Error("Error reportUser: ${e.message}", e)
        }
    }
}