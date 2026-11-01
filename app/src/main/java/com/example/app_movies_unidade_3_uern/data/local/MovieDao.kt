package com.example.app_movies_unidade_3_uern.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    /**
     * Retorna a lista reativa de filmes marcados como favoritos.
     */
    @Query("SELECT * FROM movies WHERE isFavorite = 1 ORDER BY title ASC")
    fun getFavoriteMovies(): Flow<List<MovieEntity>>

    /**
     * Busca um filme específico pelo ID (imdbID) para consumo pontual ou verificação de cache.
     */
    @Query("SELECT * FROM movies WHERE id = :id LIMIT 1")
    suspend fun getMovieById(id: String): MovieEntity?

    /**
     * Retorna um fluxo reativo de um filme específico pelo ID.
     */
    @Query("SELECT * FROM movies WHERE id = :id LIMIT 1")
    fun getMovieByIdFlow(id: String): Flow<MovieEntity?>

    /**
     * Query de Upsert customizada:
     * Insere o filme caso não exista. Caso haja conflito de chave primária (id),
     * atualiza os dados recebidos da API e o timestamp de cache,
     * MAS preserva intacto o status de `isFavorite` existente no banco.
     */
    @Query("""
        INSERT INTO movies (id, title, year, genre, plot, rating, posterUrl, isFavorite, lastUpdatedTimestamp)
        VALUES (:id, :title, :year, :genre, :plot, :rating, :posterUrl, :isFavorite, :lastUpdatedTimestamp)
        ON CONFLICT(id) DO UPDATE SET
            title = excluded.title,
            year = excluded.year,
            genre = excluded.genre,
            plot = excluded.plot,
            rating = excluded.rating,
            posterUrl = excluded.posterUrl,
            lastUpdatedTimestamp = excluded.lastUpdatedTimestamp
    """)
    suspend fun upsertMoviePreservingFavoriteQuery(
        id: String,
        title: String,
        year: String,
        genre: String,
        plot: String,
        rating: String,
        posterUrl: String,
        isFavorite: Boolean,
        lastUpdatedTimestamp: Long
    )

    /**
     * Wrapper de conveniência que recebe diretamente a entidade MovieEntity.
     */
    suspend fun upsertMoviePreservingFavorite(movie: MovieEntity) {
        upsertMoviePreservingFavoriteQuery(
            id = movie.id,
            title = movie.title,
            year = movie.year,
            genre = movie.genre,
            plot = movie.plot,
            rating = movie.rating,
            posterUrl = movie.posterUrl,
            isFavorite = movie.isFavorite,
            lastUpdatedTimestamp = movie.lastUpdatedTimestamp
        )
    }

    /**
     * Alternativa transacional segura via Kotlin:
     * Verifica se o registro já existe para reter o status de favorito.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovie(movie: MovieEntity): Long

    @Update
    suspend fun updateMovie(movie: MovieEntity)

    @Transaction
    suspend fun upsertWithFavoritePreserved(movie: MovieEntity) {
        val existing = getMovieById(movie.id)
        if (existing == null) {
            insertMovie(movie)
        } else {
            updateMovie(movie.copy(isFavorite = existing.isFavorite))
        }
    }

    /**
     * Atualiza exclusivamente o status de favorito de um filme específico.
     */
    @Query("UPDATE movies SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean)

    /**
     * Deleta filmes que não sejam favoritos e que estejam com cache expirado (opcional para limpeza).
     */
    @Query("DELETE FROM movies WHERE isFavorite = 0 AND lastUpdatedTimestamp < :expirationTimestamp")
    suspend fun clearExpiredCache(expirationTimestamp: Long)
}
