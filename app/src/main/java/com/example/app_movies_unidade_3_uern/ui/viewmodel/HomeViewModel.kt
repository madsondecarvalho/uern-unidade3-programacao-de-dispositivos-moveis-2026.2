package com.example.app_movies_unidade_3_uern.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_movies_unidade_3_uern.data.remote.dto.MovieSummaryDto
import com.example.app_movies_unidade_3_uern.data.repository.MovieRepository
import com.example.app_movies_unidade_3_uern.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("Batman")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _moviesState = MutableStateFlow<UiState<List<MovieSummaryDto>>>(UiState.Idle)
    val moviesState: StateFlow<UiState<List<MovieSummaryDto>>> = _moviesState.asStateFlow()

    init {
        // Realiza uma busca inicial amigável ao iniciar a tela
        searchMovies(_searchQuery.value)
    }

    fun onQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun searchMovies(query: String = _searchQuery.value) {
        val target = query.trim()
        if (target.isBlank()) {
            _moviesState.value = UiState.Idle
            return
        }

        viewModelScope.launch {
            _moviesState.value = UiState.Loading
            val result = repository.searchMovies(target)
            result.fold(
                onSuccess = { list ->
                    _moviesState.value = UiState.Success(list)
                },
                onFailure = { error ->
                    _moviesState.value = UiState.Error(error.localizedMessage ?: "Erro ao buscar filmes.")
                }
            )
        }
    }
}
