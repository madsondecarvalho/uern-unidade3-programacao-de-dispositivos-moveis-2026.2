package com.example.app_movies_unidade_3_uern

import com.example.app_movies_unidade_3_uern.data.local.MovieReviewEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Testes unitários para a entidade de avaliação (MovieReviewEntity),
 * garantindo compatibilidade com caracteres especiais brasileiros (ã, ç, é, ô, etc.)
 * e validação correta dos dados.
 */
class MovieReviewEntityTest {

    @Test
    fun movieReview_preservesBrazilianSpecialCharacters() {
        val commentWithAccents = "Excelente direção! As atuações têm emoção e o final é uma lição de superação."
        val movieTitleWithAccents = "Capitão Fantástico & O Auto da Compadecida"

        val review = MovieReviewEntity(
            movieId = "tt1234567",
            movieTitle = movieTitleWithAccents,
            moviePosterUrl = "https://example.com/poster.jpg",
            movieYear = "2020",
            rating = 5,
            comment = commentWithAccents
        )

        assertEquals("Capitão Fantástico & O Auto da Compadecida", review.movieTitle)
        assertEquals(commentWithAccents, review.comment)
        assertTrue(review.comment.contains("ã"))
        assertTrue(review.comment.contains("ç"))
        assertTrue(review.comment.contains("õ"))
        assertTrue(review.comment.contains("é"))
        assertEquals(5, review.rating)
    }

    @Test
    fun movieReview_ratingBoundaryCheck() {
        val minReview = MovieReviewEntity(
            movieId = "tt001",
            movieTitle = "Filme Teste",
            moviePosterUrl = "",
            movieYear = "2021",
            rating = 1,
            comment = "Regular"
        )
        val maxReview = MovieReviewEntity(
            movieId = "tt002",
            movieTitle = "Filme Obra-Prima",
            moviePosterUrl = "",
            movieYear = "2022",
            rating = 5,
            comment = "Sensacional! Vale cada minuto."
        )

        assertEquals(1, minReview.rating)
        assertEquals(5, maxReview.rating)
        assertTrue(minReview.timestamp > 0)
        assertTrue(maxReview.timestamp > 0)
    }
}
