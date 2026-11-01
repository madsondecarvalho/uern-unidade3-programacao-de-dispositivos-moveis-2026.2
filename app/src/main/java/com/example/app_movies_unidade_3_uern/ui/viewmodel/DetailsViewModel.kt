package com.example.app_movies_unidade_3_uern.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_movies_unidade_3_uern.data.local.MovieEntity
import com.example.app_movies_unidade_3_uern.data.local.MovieReviewEntity
import com.example.app_movies_unidade_3_uern.data.repository.MovieRepository
import com.example.app_movies_unidade_3_uern.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _detailsState = MutableStateFlow<UiState<MovieEntity>>(UiState.Loading)
    val detailsState: StateFlow<UiState<MovieEntity>> = _detailsState.asStateFlow()

    private val _userReview = MutableStateFlow<MovieReviewEntity?>(null)
    val userReview: StateFlow<MovieReviewEntity?> = _userReview.asStateFlow()

    fun loadMovieDetails(movieId: String) {
        viewModelScope.launch {
            _detailsState.value = UiState.Loading
            val result = repository.getMovieDetails(movieId)
            result.fold(
                onSuccess = { entity ->
                    _detailsState.value = UiState.Success(entity)
                },
                onFailure = { error ->
                    _detailsState.value = UiState.Error(
                        error.localizedMessage ?: "Não foi possível carregar os detalhes do filme."
                    )
                }
            )
        }

        // Monitora reativamente se o usuário já avaliou este filme no SQLite
        viewModelScope.launch {
            repository.getReviewByMovieId(movieId).collect { review ->
                _userReview.value = review
            }
        }
    }

    fun toggleFavorite(movie: MovieEntity) {
        viewModelScope.launch {
            val newFavoriteStatus = !movie.isFavorite
            repository.toggleFavorite(movie.id, newFavoriteStatus)
            _detailsState.value = UiState.Success(movie.copy(isFavorite = newFavoriteStatus))
        }
    }

    /**
     * Salva ou atualiza a avaliação e comentário do usuário no SQLite (Room).
     */
    fun saveReview(movie: MovieEntity, rating: Int, comment: String) {
        viewModelScope.launch {
            val review = MovieReviewEntity(
                movieId = movie.id,
                movieTitle = movie.title,
                moviePosterUrl = movie.posterUrl,
                movieYear = movie.year,
                rating = rating,
                comment = comment,
                timestamp = System.currentTimeMillis()
            )
            repository.saveReview(review)
        }
    }

    /**
     * Remove a avaliação do filme no SQLite.
     */
    fun deleteReview(movieId: String) {
        viewModelScope.launch {
            repository.deleteReview(movieId)
        }
    }
}
