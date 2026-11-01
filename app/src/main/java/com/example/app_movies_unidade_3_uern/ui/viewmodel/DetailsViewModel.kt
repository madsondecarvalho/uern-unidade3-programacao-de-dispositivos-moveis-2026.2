package com.example.app_movies_unidade_3_uern.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_movies_unidade_3_uern.data.local.MovieEntity
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
    }

    fun toggleFavorite(movie: MovieEntity) {
        viewModelScope.launch {
            val newFavoriteStatus = !movie.isFavorite
            repository.toggleFavorite(movie.id, newFavoriteStatus)
            // Atualiza o estado da tela instantaneamente para feedback imediato na UI
            _detailsState.value = UiState.Success(movie.copy(isFavorite = newFavoriteStatus))
        }
    }
}
