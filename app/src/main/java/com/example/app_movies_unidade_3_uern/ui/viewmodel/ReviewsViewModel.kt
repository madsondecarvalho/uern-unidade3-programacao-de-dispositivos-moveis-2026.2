package com.example.app_movies_unidade_3_uern.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_movies_unidade_3_uern.data.local.MovieReviewEntity
import com.example.app_movies_unidade_3_uern.data.repository.MovieRepository
import com.example.app_movies_unidade_3_uern.ui.state.UiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReviewsViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    val reviewsState: StateFlow<UiState<List<MovieReviewEntity>>> = repository.getAllReviews()
        .map<List<MovieReviewEntity>, UiState<List<MovieReviewEntity>>> { list ->
            UiState.Success(list)
        }
        .catch { e ->
            emit(UiState.Error(e.localizedMessage ?: "Erro ao carregar avaliações do SQLite."))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
        )

    fun deleteReview(movieId: String) {
        viewModelScope.launch {
            repository.deleteReview(movieId)
        }
    }
}
