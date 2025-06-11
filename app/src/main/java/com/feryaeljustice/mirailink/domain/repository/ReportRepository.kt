package com.feryaeljustice.mirailink.domain.repository

import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

interface ReportRepository {
    suspend fun reportUser(reportedUser: String, reason: String): MiraiLinkResult<Unit>
}