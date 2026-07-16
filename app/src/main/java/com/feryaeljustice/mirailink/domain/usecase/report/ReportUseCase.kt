package com.feryaeljustice.mirailink.domain.usecase.report

import com.feryaeljustice.mirailink.domain.repository.ReportRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class ReportUseCase(
    private val repository: ReportRepository,
) {
    suspend operator fun invoke(
        reportUser: String,
        reason: String,
    ): MiraiLinkResult<Unit> =
        repository.reportUser(reportUser, reason)
}
