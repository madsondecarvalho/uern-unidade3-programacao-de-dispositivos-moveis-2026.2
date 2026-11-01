package com.example.app_movies_unidade_3_uern.data.repository

import com.example.app_movies_unidade_3_uern.data.local.MovieDao
import com.example.app_movies_unidade_3_uern.data.local.MovieEntity
import com.example.app_movies_unidade_3_uern.data.local.MovieReviewDao
import com.example.app_movies_unidade_3_uern.data.local.MovieReviewEntity
import com.example.app_movies_unidade_3_uern.data.remote.OmdbApi
import com.example.app_movies_unidade_3_uern.data.remote.dto.MovieSummaryDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MovieRepository(
    private val omdbApi: OmdbApi,
    private val movieDao: MovieDao,
    private val movieReviewDao: MovieReviewDao
) {

    companion object {
        /**
         * Top 10 filmes mais bem avaliados de todos os tempos segundo o IMDb.
         * URLs atualizadas e verificadas diretamente via OMDb API.
         */
        val TOP_RATED_MOVIES = listOf(
            MovieSummaryDto(
                title = "The Shawshank Redemption",
                year = "1994",
                imdbId = "tt0111161",
                type = "9.3 ★ • Top #1",
                poster = "https://m.media-amazon.com/images/M/MV5BMDAyY2FhYjctNDc5OS00MDNlLThiMGUtY2UxYWVkNGY2ZjljXkEyXkFqcGc@._V1_QL75_UX380_CR0,4,380,562_.jpg"
            ),
            MovieSummaryDto(
                title = "The Godfather",
                year = "1972",
                imdbId = "tt0068646",
                type = "9.2 ★ • Top #2",
                poster = "https://m.media-amazon.com/images/M/MV5BNGEwYjgwOGQtYjg5ZS00Njc1LTk2ZGEtM2QwZWQ2NjdhZTE5XkEyXkFqcGc@._V1_QL75_UY562_CR8,0,380,562_.jpg"
            ),
            MovieSummaryDto(
                title = "The Dark Knight",
                year = "2008",
                imdbId = "tt0468569",
                type = "9.1 ★ • Top #3",
                poster = "https://m.media-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_QL75_UX380_CR0,0,380,562_.jpg"
            ),
            MovieSummaryDto(
                title = "The Godfather Part II",
                year = "1974",
                imdbId = "tt0071562",
                type = "9.0 ★ • Top #4",
                poster = "https://m.media-amazon.com/images/M/MV5BMDIxMzBlZDktZjMxNy00ZGI4LTgxNDEtYWRlNzRjMjJmOGQ1XkEyXkFqcGc@._V1_QL75_UX380_CR0,4,380,562_.jpg"
            ),
            MovieSummaryDto(
                title = "12 Angry Men",
                year = "1957",
                imdbId = "tt0050083",
                type = "9.0 ★ • Top #5",
                poster = "https://m.media-amazon.com/images/M/MV5BYjE4NzdmOTYtYjc5Yi00YzBiLWEzNDEtNTgxZGQ2MWVkN2NiXkEyXkFqcGc@._V1_QL75_UX380_CR0,11,380,562_.jpg"
            ),
            MovieSummaryDto(
                title = "Schindler's List",
                year = "1993",
                imdbId = "tt0108052",
                type = "9.0 ★ • Top #6",
                poster = "https://m.media-amazon.com/images/M/MV5BNjM1ZDQxYWUtMzQyZS00MTE1LWJmZGYtNGUyNTdlYjM3ZmVmXkEyXkFqcGc@._V1_QL75_UX380_CR0,4,380,562_.jpg"
            ),
            MovieSummaryDto(
                title = "The Lord of the Rings: The Return of the King",
                year = "2003",
                imdbId = "tt0167260",
                type = "9.0 ★ • Top #7",
                poster = "https://m.media-amazon.com/images/M/MV5BMTZkMjBjNWMtZGI5OC00MGU0LTk4ZTItODg2NWM3NTVmNWQ4XkEyXkFqcGc@._V1_QL75_UX380_CR0,0,380,562_.jpg"
            ),
            MovieSummaryDto(
                title = "Pulp Fiction",
                year = "1994",
                imdbId = "tt0110912",
                type = "8.8 ★ • Top #8",
                poster = "https://m.media-amazon.com/images/M/MV5BYTViYTE3ZGQtNDBlMC00ZTAyLTkyODMtZGRiZDg0MjA2YThkXkEyXkFqcGc@._V1_QL75_UY562_CR3,0,380,562_.jpg"
            ),
            MovieSummaryDto(
                title = "The Lord of the Rings: The Fellowship of the Ring",
                year = "2001",
                imdbId = "tt0120737",
                type = "8.9 ★ • Top #9",
                poster = "https://m.media-amazon.com/images/M/MV5BNzIxMDQ2YTctNDY4MC00ZTRhLTk4ODQtMTVlOWY4NTdiYmMwXkEyXkFqcGc@._V1_QL75_UX380_CR0,1,380,562_.jpg"
            ),
            MovieSummaryDto(
                title = "Fight Club",
                year = "1999",
                imdbId = "tt0137523",
                type = "8.8 ★ • Top #10",
                poster = "https://m.media-amazon.com/images/M/MV5BOTgyOGQ1NDItNGU3Ny00MjU3LTg2YWEtNmEyYjBiMjI1Y2M5XkEyXkFqcGc@._V1_QL75_UX380_CR0,4,380,562_.jpg"
            )
        )
    }

    /**
     * Retorna os 10 filmes mais bem avaliados do catálogo.
     */
    fun getTopRatedMovies(): List<MovieSummaryDto> = TOP_RATED_MOVIES

    /**
     * Busca a lista de filmes na API OMDb pelo termo pesquisado.
     * Retorna os resumos para exibição na UI e normaliza URLs para HTTPS.
     */
    suspend fun searchMovies(query: String): Result<List<MovieSummaryDto>> = withContext(Dispatchers.IO) {
        try {
            val sanitizedQuery = query.trim()
            if (sanitizedQuery.isBlank()) {
                return@withContext Result.success(emptyList())
            }

            val response = omdbApi.searchMovies(sanitizedQuery)
            if (response.response.equals("True", ignoreCase = true) && response.search != null) {
                // Normaliza pôsteres para HTTPS e recupera do cache local se a busca retornou sem pôster
                val normalizedList = response.search.map { movie ->
                    var posterUrl = movie.poster?.trim()
                    if (posterUrl.isNullOrBlank() || posterUrl == "N/A") {
                        val cached = movieDao.getMovieById(movie.imdbId)
                        if (cached != null && cached.posterUrl.isNotBlank() && cached.posterUrl != "N/A") {
                            posterUrl = cached.posterUrl
                        }
                    }
                    if (posterUrl != null && posterUrl.startsWith("http://", ignoreCase = true)) {
                        posterUrl = posterUrl.replaceFirst("http://", "https://", ignoreCase = true)
                    }
                    movie.copy(poster = posterUrl)
                }
                Result.success(normalizedList)
            } else {
                Result.failure(Exception(response.error ?: "Nenhum filme encontrado para '$sanitizedQuery'."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Falha na conexão: ${e.localizedMessage ?: "Erro desconhecido"}", e))
        }
    }

    /**
     * Arquitetura Offline-First:
     * O SQLite (Room) atua como fonte única da verdade.
     * Conforme o aplicativo é utilizado, o SQLite armazena os dados permanentemente,
     * permitindo navegação completa mesmo sem conexão com a internet.
     */
    suspend fun getMovieDetails(id: String): Result<MovieEntity> = withContext(Dispatchers.IO) {
        try {
            val cachedMovie = movieDao.getMovieById(id)

            // 1. Se já existe no Room com detalhes completos, retorna imediatamente do SQLite (Permanente)
            if (cachedMovie != null && cachedMovie.plot.isNotBlank()) {
                return@withContext Result.success(cachedMovie)
            }

            // 2. Não está no banco: busca na API OMDb
            val dto = omdbApi.getMovieDetails(id)
            if (dto.response.equals("True", ignoreCase = true)) {
                var posterUrl = dto.poster?.trim() ?: ""
                if (posterUrl.startsWith("http://", ignoreCase = true)) {
                    posterUrl = posterUrl.replaceFirst("http://", "https://", ignoreCase = true)
                }

                val updatedEntity = MovieEntity(
                    id = dto.imdbId,
                    title = dto.title ?: cachedMovie?.title ?: "Sem título",
                    year = dto.year ?: cachedMovie?.year ?: "",
                    genre = dto.genre ?: "",
                    plot = dto.plot ?: "",
                    rating = dto.imdbRating ?: "",
                    posterUrl = posterUrl,
                    isFavorite = cachedMovie?.isFavorite ?: false,
                    lastUpdatedTimestamp = System.currentTimeMillis()
                )

                // 3. Upsert no Room sem sobrescrever favoritos do usuário
                movieDao.upsertMoviePreservingFavorite(updatedEntity)
                val savedMovie = movieDao.getMovieById(id) ?: updatedEntity
                Result.success(savedMovie)
            } else {
                if (cachedMovie != null) {
                    Result.success(cachedMovie)
                } else {
                    Result.failure(Exception(dto.error ?: "Detalhes do filme não encontrados."))
                }
            }
        } catch (e: Exception) {
            // Em caso de falha de conexão (offline), retorna o dado do banco se existir
            val fallbackMovie = movieDao.getMovieById(id)
            if (fallbackMovie != null) {
                Result.success(fallbackMovie)
            } else {
                Result.failure(Exception("Sem conexão e filme não encontrado no cache offline: ${e.localizedMessage}", e))
            }
        }
    }

    /**
     * Alterna o status de favorito do filme no Room.
     */
    suspend fun toggleFavorite(id: String, isFavorite: Boolean) = withContext(Dispatchers.IO) {
        movieDao.updateFavoriteStatus(id, isFavorite)
    }

    /**
     * Retorna o fluxo reativo dos filmes favoritos salvos localmente.
     */
    fun getFavoriteMovies(): Flow<List<MovieEntity>> {
        return movieDao.getFavoriteMovies()
    }

    // ========================================================================
    // Gestão de Avaliações do Usuário (SQLite - Room)
    // ========================================================================

    /**
     * Retorna o fluxo reativo de todas as avaliações e comentários salvos no SQLite.
     */
    fun getAllReviews(): Flow<List<MovieReviewEntity>> = movieReviewDao.getAllReviews()

    /**
     * Retorna o fluxo reativo da avaliação de um filme específico.
     */
    fun getReviewByMovieId(movieId: String): Flow<MovieReviewEntity?> =
        movieReviewDao.getReviewByMovieId(movieId)

    /**
     * Salva ou atualiza a avaliação (1 a 5 estrelas e comentário) no SQLite.
     */
    suspend fun saveReview(review: MovieReviewEntity) = withContext(Dispatchers.IO) {
        movieReviewDao.insertOrUpdateReview(review)
    }

    /**
     * Remove uma avaliação do banco local.
     */
    suspend fun deleteReview(movieId: String) = withContext(Dispatchers.IO) {
        movieReviewDao.deleteReview(movieId)
    }
}
