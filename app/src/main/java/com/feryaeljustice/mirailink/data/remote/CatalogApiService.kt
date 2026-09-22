package com.feryaeljustice.mirailink.data.remote

import com.feryaeljustice.mirailink.data.model.AnimeDto
import com.feryaeljustice.mirailink.data.model.GameDto
import com.feryaeljustice.mirailink.data.model.GeographicPlaceDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CatalogApiService {
    @GET("catalog/animes")
    suspend fun getAllAnimes(): List<AnimeDto>

    @GET("catalog/games")
    suspend fun getAllGames(): List<GameDto>

    @GET("catalog/geography/countries")
    suspend fun getCountries(): List<GeographicPlaceDto>

    @GET("catalog/geography/countries/{countryId}/regions")
    suspend fun getRegions(@Path("countryId") countryId: String): List<GeographicPlaceDto>

    @GET("catalog/geography/regions/{regionId}/cities")
    suspend fun getCities(
        @Path("regionId") regionId: String,
        @Query("query") query: String,
    ): List<GeographicPlaceDto>
}
