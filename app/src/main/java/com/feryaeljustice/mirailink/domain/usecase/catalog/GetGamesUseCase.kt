package com.feryaeljustice.mirailink.domain.usecase.catalog

import android.util.Log
import com.feryaeljustice.mirailink.domain.model.Game
import com.feryaeljustice.mirailink.domain.repository.CatalogRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class GetGamesUseCase @Inject constructor(private val repository: CatalogRepository) {
    suspend operator fun invoke(): MiraiLinkResult<List<Game>> {
        val games = repository.getGames()
        Log.d("GetGamesUseCase", "Games: $games")
        return games
    }
}