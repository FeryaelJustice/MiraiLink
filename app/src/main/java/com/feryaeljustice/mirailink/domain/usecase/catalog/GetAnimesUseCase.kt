package com.feryaeljustice.mirailink.domain.usecase.catalog

import android.util.Log
import com.feryaeljustice.mirailink.domain.model.Anime
import com.feryaeljustice.mirailink.domain.repository.CatalogRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class GetAnimesUseCase @Inject constructor(private val repository: CatalogRepository) {
    suspend operator fun invoke(): MiraiLinkResult<List<Anime>> {
        val animes = repository.getAnimes()
        Log.d("GetAnimesUseCase", "Animes: $animes")
        return animes
    }
}