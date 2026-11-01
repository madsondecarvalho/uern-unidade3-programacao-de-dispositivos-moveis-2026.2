package com.example.app_movies_unidade_3_uern.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.app_movies_unidade_3_uern.data.remote.NetworkMonitor
import com.example.app_movies_unidade_3_uern.data.repository.MovieRepository

class ViewModelFactory(
    private val repository: MovieRepository,
    private val networkMonitor: NetworkMonitor? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository, networkMonitor) as T
            }
            modelClass.isAssignableFrom(DetailsViewModel::class.java) -> {
                DetailsViewModel(repository) as T
            }
            modelClass.isAssignableFrom(FavoritesViewModel::class.java) -> {
                FavoritesViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ReviewsViewModel::class.java) -> {
                ReviewsViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("ViewModel desconhecida: ${modelClass.name}")
        }
    }
}
