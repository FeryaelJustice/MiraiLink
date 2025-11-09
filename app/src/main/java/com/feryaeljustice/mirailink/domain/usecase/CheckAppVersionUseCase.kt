package com.feryaeljustice.mirailink.domain.usecase

import com.feryaeljustice.mirailink.domain.model.VersionCheckResult
import com.feryaeljustice.mirailink.domain.repository.AppConfigRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class CheckAppVersionUseCase(
    private val repo: AppConfigRepository,
) {
    suspend operator fun invoke(currentVersionCode: Int): MiraiLinkResult<VersionCheckResult> =
        when (val r = repo.getVersion()) {
            is MiraiLinkResult.Success -> {
                val server = r.data
                val mustUpdate = currentVersionCode < server.minVersionCode
                val shouldUpdate = !mustUpdate && currentVersionCode < server.latestVersionCode
                MiraiLinkResult.success(
                    VersionCheckResult(
                        mustUpdate = mustUpdate,
                        shouldUpdate = shouldUpdate,
                        message = server.message.orEmpty(),
                        playStoreUrl = server.playStoreUrl,
                    ),
                )
            }

            is MiraiLinkResult.Error -> MiraiLinkResult.error(r.message, r.exception)
        }
}
