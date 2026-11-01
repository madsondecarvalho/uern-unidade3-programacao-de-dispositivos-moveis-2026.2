package com.example.app_movies_unidade_3_uern.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidade para persistência das avaliações e resenhas do usuário no SQLite (Room).
 * Suporta nota de 1 a 5 estrelas e comentário livre sobre o filme.
 */
@Entity(tableName = "movie_reviews")
data class MovieReviewEntity(
    @PrimaryKey
    val movieId: String,             // ID único no IMDb (ex: "tt0111161")
    val movieTitle: String,          // Título do filme para exibição offline imediata
    val moviePosterUrl: String,      // Pôster do filme
    val movieYear: String,           // Ano de lançamento
    val rating: Int,                 // Avaliação dada pelo usuário (1 a 5 estrelas)
    val comment: String,             // Comentário ou resenha escrita pelo usuário
    val timestamp: Long = System.currentTimeMillis() // Data/hora da avaliação
)
