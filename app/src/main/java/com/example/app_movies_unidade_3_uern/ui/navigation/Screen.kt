package com.example.app_movies_unidade_3_uern.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object MovieList : Screen("movie_list")
    data object MovieDetails : Screen("movie_details/{movieId}") {
        fun createRoute(movieId: String): String = "movie_details/$movieId"
    }
    data object Favorites : Screen("favorites")
}
