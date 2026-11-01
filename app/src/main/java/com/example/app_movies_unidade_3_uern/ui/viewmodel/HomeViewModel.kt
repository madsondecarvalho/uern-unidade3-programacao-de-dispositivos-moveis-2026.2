package com.example.app_movies_unidade_3_uern.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_movies_unidade_3_uern.data.remote.NetworkMonitor
import com.example.app_movies_unidade_3_uern.data.remote.dto.MovieSummaryDto
import com.example.app_movies_unidade_3_uern.data.repository.MovieRepository
import com.example.app_movies_unidade_3_uern.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: MovieRepository,
    private val networkMonitor: NetworkMonitor? = null
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _moviesState = MutableStateFlow<UiState<List<MovieSummaryDto>>>(UiState.Loading)
    val moviesState: StateFlow<UiState<List<MovieSummaryDto>>> = _moviesState.asStateFlow()

    private val _isShowingTopRated = MutableStateFlow(true)
    val isShowingTopRated: StateFlow<Boolean> = _isShowingTopRated.asStateFlow()

    // Monitoramento de conexão em tempo real
    val isOnline: StateFlow<Boolean> = networkMonitor?.isOnline?.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = networkMonitor.isCurrentlyOnline()
    ) ?: MutableStateFlow(true).asStateFlow()

    init {
        // Por padrão, carrega os 10 filmes mais bem avaliados (sem busca fixa de Batman)
        loadTopRatedMovies()
    }

    fun onQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
        if (newQuery.isBlank()) {
            loadTopRatedMovies()
        }
    }

    /**
     * Exibe o Top 10 mais bem avaliados da história do cinema.
     */
    fun loadTopRatedMovies() {
        _isShowingTopRated.value = true
        _moviesState.value = UiState.Success(repository.getTopRatedMovies())
    }

    fun searchMovies(query: String = _searchQuery.value) {
        val target = query.trim()
        if (target.isBlank()) {
            loadTopRatedMovies()
            return
        }

        // Aviso proativo caso o dispositivo esteja sem internet
        if (networkMonitor?.isCurrentlyOnline() == false) {
            _isShowingTopRated.value = false
            _moviesState.value = UiState.Error(
                "Você está sem conexão com a internet. O modo offline permite visualizar favoritos e avaliações salvos no SQLite, mas novas buscas na API exigem conexão."
            )
            return
        }

        viewModelScope.launch {
            _isShowingTopRated.value = false
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
