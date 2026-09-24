package com.feryaeljustice.mirailink.domain.usecase.explore

import com.feryaeljustice.mirailink.domain.repository.ExploreHubData
import com.feryaeljustice.mirailink.domain.repository.ExploreRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class GetExploreSectionsUseCase(
    private val repository: ExploreRepository,
) {
    suspend operator fun invoke(): MiraiLinkResult<ExploreHubData> =
        repository.getExploreHubData()
}
