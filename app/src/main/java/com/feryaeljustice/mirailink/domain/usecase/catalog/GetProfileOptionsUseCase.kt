package com.feryaeljustice.mirailink.domain.usecase.catalog

import com.feryaeljustice.mirailink.data.model.response.catalog.ProfileOptionsResponseDto
import com.feryaeljustice.mirailink.domain.repository.CatalogRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class GetProfileOptionsUseCase(
    private val repository: CatalogRepository,
) {
    suspend operator fun invoke(): MiraiLinkResult<ProfileOptionsResponseDto> =
        repository.getProfileOptions()
}
