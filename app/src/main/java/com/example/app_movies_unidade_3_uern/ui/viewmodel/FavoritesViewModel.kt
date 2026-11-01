package com.example.app_movies_unidade_3_uern.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_movies_unidade_3_uern.data.local.MovieEntity
import com.example.app_movies_unidade_3_uern.data.repository.MovieRepository
import com.example.app_movies_unidade_3_uern.ui.state.UiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    val favoritesState: StateFlow<UiState<List<MovieEntity>>> = repository.getFavoriteMovies()
        .map<List<MovieEntity>, UiState<List<MovieEntity>>> { list ->
            UiState.Success(list)
        }
        .catch { e ->
            emit(UiState.Error(e.localizedMessage ?: "Erro ao carregar favoritos."))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
        )

    fun removeFromFavorites(movieId: String) {
        viewModelScope.launch {
            repository.toggleFavorite(movieId, false)
        }
    }
}
