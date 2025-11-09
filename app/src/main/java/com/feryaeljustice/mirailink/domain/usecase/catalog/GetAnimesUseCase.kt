/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.catalog

import com.feryaeljustice.mirailink.domain.model.catalog.Anime
import com.feryaeljustice.mirailink.domain.repository.CatalogRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class GetAnimesUseCase(
    private val repository: CatalogRepository,
) {
    suspend operator fun invoke(): MiraiLinkResult<List<Anime>> =
        try {
            repository.getAnimes()
        } catch (e: Exception) {
            MiraiLinkResult.Error("An error occurred while getting the animes", e)
        }
}
