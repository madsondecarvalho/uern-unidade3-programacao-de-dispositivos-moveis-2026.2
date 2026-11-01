package com.example.app_movies_unidade_3_uern

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.app_movies_unidade_3_uern.data.local.AppDatabase
import com.example.app_movies_unidade_3_uern.data.remote.NetworkModule
import com.example.app_movies_unidade_3_uern.data.remote.NetworkMonitor
import com.example.app_movies_unidade_3_uern.data.repository.MovieRepository
import com.example.app_movies_unidade_3_uern.ui.navigation.AppNavHost
import com.example.app_movies_unidade_3_uern.ui.theme.Appmoviesunidade3uernTheme
import com.example.app_movies_unidade_3_uern.ui.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicialização dos componentes da arquitetura MVVM Offline-First
        val database = AppDatabase.getDatabase(applicationContext)
        val omdbApi = NetworkModule.omdbApi
        val networkMonitor = NetworkMonitor(applicationContext)
        val movieRepository = MovieRepository(omdbApi, database.movieDao(), database.movieReviewDao())
        val viewModelFactory = ViewModelFactory(movieRepository, networkMonitor)

        setContent {
            Appmoviesunidade3uernTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost(viewModelFactory = viewModelFactory)
                }
            }
        }
    }
}
