package com.example.app_movies_unidade_3_uern.data.repository

import com.example.app_movies_unidade_3_uern.data.local.MovieDao
import com.example.app_movies_unidade_3_uern.data.local.MovieEntity
import com.example.app_movies_unidade_3_uern.data.remote.OmdbApi
import com.example.app_movies_unidade_3_uern.data.remote.dto.MovieSummaryDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MovieRepository(
    private val omdbApi: OmdbApi,
    private val movieDao: MovieDao
) {

    companion object {
        // 24 horas em milissegundos
        private const val ONE_DAY_MILLIS = 24 * 60 * 60 * 1000L
    }

    /**
     * Busca a lista de filmes na API OMDb pelo termo pesquisado.
     * Retorna os resumos para exibição na UI (sem necessidade de persistência).
     */
    suspend fun searchMovies(query: String): Result<List<MovieSummaryDto>> = withContext(Dispatchers.IO) {
        try {
            val sanitizedQuery = query.trim()
            if (sanitizedQuery.isBlank()) {
                return@withContext Result.success(emptyList())
            }

            val response = omdbApi.searchMovies(sanitizedQuery)
            if (response.response.equals("True", ignoreCase = true) && response.search != null) {
                Result.success(response.search)
            } else {
                Result.failure(Exception(response.error ?: "Nenhum filme encontrado para '$sanitizedQuery'."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Falha na conexão: ${e.localizedMessage ?: "Erro desconhecido"}", e))
        }
    }

    /**
     * Lógica de Cache de 1 Dia:
     * 1. Consulta o Room pelo ID.
     * 2. Verifica se existe, se possui detalhes completos e se o cache tem menos de 24 horas.
     *    Se válido, retorna imediatamente o dado local.
     * 3. Caso contrário, faz a requisição na API via Retrofit.
     * 4. Mapeia para MovieEntity, atualiza o timestamp, faz Upsert preservando isFavorite
     *    e retorna a entidade atualizada.
     */
    suspend fun getMovieDetails(id: String): Result<MovieEntity> = withContext(Dispatchers.IO) {
        try {
            val cachedMovie = movieDao.getMovieById(id)
            val currentTime = System.currentTimeMillis()

            // 1. Checagem do Cache de 24h
            val isCacheValid = cachedMovie != null &&
                    cachedMovie.plot.isNotBlank() &&
                    (currentTime - cachedMovie.lastUpdatedTimestamp) < ONE_DAY_MILLIS

            if (isCacheValid) {
                return@withContext Result.success(cachedMovie!!)
            }

            // 2. Cache inexistente ou expirado (>24h): buscar na API
            val dto = omdbApi.getMovieDetails(id)
            if (dto.response.equals("True", ignoreCase = true)) {
                val updatedEntity = MovieEntity(
                    id = dto.imdbId,
                    title = dto.title ?: cachedMovie?.title ?: "Sem título",
                    year = dto.year ?: cachedMovie?.year ?: "",
                    genre = dto.genre ?: "",
                    plot = dto.plot ?: "",
                    rating = dto.imdbRating ?: "",
                    posterUrl = dto.poster ?: "",
                    // Preserva o status de favorito que já existia no banco local
                    isFavorite = cachedMovie?.isFavorite ?: false,
                    lastUpdatedTimestamp = currentTime
                )

                // 3. Upsert no Room sem sobrescrever favorito
                movieDao.upsertMoviePreservingFavorite(updatedEntity)

                // Retorna o registro recém-atualizado do banco
                val savedMovie = movieDao.getMovieById(id) ?: updatedEntity
                Result.success(savedMovie)
            } else {
                // Fallback offline caso a API responda com erro mas haja dados em cache
                if (cachedMovie != null) {
                    Result.success(cachedMovie)
                } else {
                    Result.failure(Exception(dto.error ?: "Detalhes do filme não encontrados."))
                }
            }
        } catch (e: Exception) {
            // Em caso de falta de conexão, se houver cache local (mesmo expirado), retorna como fallback
            val fallbackMovie = movieDao.getMovieById(id)
            if (fallbackMovie != null) {
                Result.success(fallbackMovie)
            } else {
                Result.failure(Exception("Não foi possível carregar os detalhes: ${e.localizedMessage}", e))
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
}
