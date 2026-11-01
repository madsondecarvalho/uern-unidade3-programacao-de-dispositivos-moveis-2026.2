package com.example.app_movies_unidade_3_uern.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieReviewDao {

    /**
     * Retorna a lista de todas as avaliações do usuário salvas no SQLite,
     * ordenadas da mais recente para a mais antiga.
     */
    @Query("SELECT * FROM movie_reviews ORDER BY timestamp DESC")
    fun getAllReviews(): Flow<List<MovieReviewEntity>>

    /**
     * Retorna um fluxo reativo da avaliação de um filme específico pelo ID.
     */
    @Query("SELECT * FROM movie_reviews WHERE movieId = :movieId LIMIT 1")
    fun getReviewByMovieId(movieId: String): Flow<MovieReviewEntity?>

    /**
     * Consulta síncrona/direta da avaliação de um filme.
     */
    @Query("SELECT * FROM movie_reviews WHERE movieId = :movieId LIMIT 1")
    suspend fun getReviewSync(movieId: String): MovieReviewEntity?

    /**
     * Insere ou atualiza uma avaliação (Upsert).
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateReview(review: MovieReviewEntity)

    /**
     * Remove a avaliação de um filme pelo ID.
     */
    @Query("DELETE FROM movie_reviews WHERE movieId = :movieId")
    suspend fun deleteReview(movieId: String)

    /**
     * Contagem total de avaliações feitas pelo usuário.
     */
    @Query("SELECT COUNT(*) FROM movie_reviews")
    fun getReviewsCount(): Flow<Int>
}
