package com.example.app_movies_unidade_3_uern.data.remote

import com.example.app_movies_unidade_3_uern.data.remote.dto.MovieDetailsDto
import com.example.app_movies_unidade_3_uern.data.remote.dto.MovieSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {

    /**
     * Busca de lista de filmes por termo de busca.
     * Exemplo: https://www.omdbapi.com/?s=Batman&apikey=...
     */
    @GET("/")
    suspend fun searchMovies(
        @Query("s") query: String,
        @Query("apikey") apiKey: String = NetworkModule.API_KEY
    ): MovieSearchResponse

    /**
     * Busca de detalhes do filme pelo ID do IMDb.
     * Exemplo: https://www.omdbapi.com/?i=tt0372784&apikey=...
     */
    @GET("/")
    suspend fun getMovieDetails(
        @Query("i") imdbId: String,
        @Query("apikey") apiKey: String = NetworkModule.API_KEY
    ): MovieDetailsDto
}
