package com.example.app_movies_unidade_3_uern.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.app_movies_unidade_3_uern.ui.screens.FavoritesScreen
import com.example.app_movies_unidade_3_uern.ui.screens.HomeScreen
import com.example.app_movies_unidade_3_uern.ui.screens.MovieDetailsScreen
import com.example.app_movies_unidade_3_uern.ui.screens.MovieListScreen
import com.example.app_movies_unidade_3_uern.ui.viewmodel.DetailsViewModel
import com.example.app_movies_unidade_3_uern.ui.viewmodel.FavoritesViewModel
import com.example.app_movies_unidade_3_uern.ui.viewmodel.HomeViewModel
import com.example.app_movies_unidade_3_uern.ui.viewmodel.ViewModelFactory

@Composable
fun AppNavHost(
    viewModelFactory: ViewModelFactory,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        // Rota: Home
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToMovieList = {
                    navController.navigate(Screen.MovieList.route)
                },
                onNavigateToFavorites = {
                    navController.navigate(Screen.Favorites.route)
                }
            )
        }

        // Rota: Lista de Filmes (Busca)
        composable(Screen.MovieList.route) {
            val homeViewModel: HomeViewModel = viewModel(factory = viewModelFactory)
            MovieListScreen(
                viewModel = homeViewModel,
                onNavigateBack = { navController.popBackStack() },
                onMovieClick = { movieId ->
                    navController.navigate(Screen.MovieDetails.createRoute(movieId))
                }
            )
        }

        // Rota: Detalhes do Filme
        composable(
            route = Screen.MovieDetails.route,
            arguments = listOf(
                navArgument("movieId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId") ?: ""
            val detailsViewModel: DetailsViewModel = viewModel(factory = viewModelFactory)
            MovieDetailsScreen(
                movieId = movieId,
                viewModel = detailsViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Rota: Favoritos
        composable(Screen.Favorites.route) {
            val favoritesViewModel: FavoritesViewModel = viewModel(factory = viewModelFactory)
            FavoritesScreen(
                viewModel = favoritesViewModel,
                onNavigateBack = { navController.popBackStack() },
                onMovieClick = { movieId ->
                    navController.navigate(Screen.MovieDetails.createRoute(movieId))
                }
            )
        }
    }
}
