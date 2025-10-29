/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.catalog

import com.feryaeljustice.mirailink.domain.model.catalog.Game
import com.feryaeljustice.mirailink.domain.repository.CatalogRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class GetGamesUseCase @Inject constructor(private val repository: CatalogRepository) {
    suspend operator fun invoke(): MiraiLinkResult<List<Game>> {
        return try {
            repository.getGames()
        } catch (e: Exception) {
            MiraiLinkResult.Error("An error occurred while getting the games", e)
        }
    }
}