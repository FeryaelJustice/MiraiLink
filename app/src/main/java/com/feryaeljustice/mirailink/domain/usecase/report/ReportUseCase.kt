package com.feryaeljustice.mirailink.domain.usecase.report

import com.feryaeljustice.mirailink.domain.repository.ReportRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class ReportUseCase @Inject constructor(private val repository: ReportRepository) {
    suspend operator fun invoke(reportUser: String, reason: String): MiraiLinkResult<Unit> {
        return try {
            repository.reportUser(reportUser, reason)
        } catch (e: Exception) {
            MiraiLinkResult.Error("ReportUseCase error", e)
        }
    }
}