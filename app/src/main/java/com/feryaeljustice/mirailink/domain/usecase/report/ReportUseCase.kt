package com.feryaeljustice.mirailink.domain.usecase.report

import android.util.Log
import com.feryaeljustice.mirailink.domain.repository.ReportRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class ReportUseCase @Inject constructor(private val repository: ReportRepository) {
    suspend operator fun invoke(reportUser: String, reason: String): MiraiLinkResult<Unit> {
        return try {
            Log.d("ReportUseCase", "Report: $reportUser $reason")
            repository.reportUser(reportUser, reason)
            MiraiLinkResult.Success(Unit)
        } catch (e: Exception) {
            Log.e("ReportUseCase", "Report error", e)
            MiraiLinkResult.Error("Error reportUser: ${e.message}", e)
        }
    }
}