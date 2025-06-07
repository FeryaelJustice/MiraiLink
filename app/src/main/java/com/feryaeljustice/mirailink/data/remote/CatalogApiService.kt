package com.feryaeljustice.mirailink.data.remote

import com.feryaeljustice.mirailink.data.model.AnimeDto
import com.feryaeljustice.mirailink.data.model.GameDto
import retrofit2.http.GET

interface CatalogApiService {
    @GET("catalog/animes")
    suspend fun getAllAnimes(): List<AnimeDto>

    @GET("catalog/games")
    suspend fun getAllGames(): List<GameDto>
}