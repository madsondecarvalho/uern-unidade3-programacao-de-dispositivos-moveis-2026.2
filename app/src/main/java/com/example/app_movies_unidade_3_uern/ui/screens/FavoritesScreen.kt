package com.example.app_movies_unidade_3_uern.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.app_movies_unidade_3_uern.data.local.MovieEntity
import com.example.app_movies_unidade_3_uern.ui.components.EmptyStateView
import com.example.app_movies_unidade_3_uern.ui.components.ErrorStateView
import com.example.app_movies_unidade_3_uern.ui.components.MovieListSkeleton
import com.example.app_movies_unidade_3_uern.ui.components.MoviePosterView
import com.example.app_movies_unidade_3_uern.ui.state.UiState
import com.example.app_movies_unidade_3_uern.ui.theme.AmberContainerDark
import com.example.app_movies_unidade_3_uern.ui.theme.AmberContainerLight
import com.example.app_movies_unidade_3_uern.ui.theme.AmberIcon
import com.example.app_movies_unidade_3_uern.ui.theme.AmberTextDark
import com.example.app_movies_unidade_3_uern.ui.theme.AmberTextLight
import com.example.app_movies_unidade_3_uern.ui.theme.CinemaRuby
import com.example.app_movies_unidade_3_uern.ui.viewmodel.FavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    onNavigateBack: () -> Unit,
    onMovieClick: (String) -> Unit,
    onExploreMovies: () -> Unit
) {
    val favoritesState by viewModel.favoritesState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Meus Filmes Favoritos",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = favoritesState) {
                is UiState.Loading -> {
                    MovieListSkeleton()
                }
                is UiState.Success -> {
                    if (state.data.isEmpty()) {
                        EmptyStateView(
                            title = "Você ainda não tem filmes favoritos",
                            message = "Explore o Top 10 mais bem avaliados ou busque qualquer título e toque no coração para salvá-lo aqui.",
                            icon = Icons.Default.Favorite,
                            actionButtonText = "Explorar Filmes",
                            onActionButtonClick = onExploreMovies
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp)
                        ) {
                            items(
                                items = state.data,
                                key = { it.id }
                            ) { movie ->
                                FavoriteMovieCard(
                                    movie = movie,
                                    onClick = { onMovieClick(movie.id) },
                                    onRemoveClick = { viewModel.removeFromFavorites(movie.id) },
                                    modifier = Modifier.animateItem()
                                )
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    ErrorStateView(
                        message = state.message,
                        onRetry = { /* Flow com stateIn reage automaticamente */ }
                    )
                }
                is UiState.Idle -> {}
            }
        }
    }
}

/**
 * Card de favorito cinematográfico:
 * - Altura mínima garantida de 132dp
 * - Letras ampliadas (19sp no título) para fácil leitura
 * - Badges com alto contraste
 * - Botão de exclusão com 48dp x 48dp
 */
@Composable
private fun FavoriteMovieCard(
    movie: MovieEntity,
    onClick: () -> Unit,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 132.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 132.dp)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Pôster ampliado (84dp x 116dp)
            MoviePosterView(
                posterUrl = movie.posterUrl,
                contentDescription = movie.title,
                modifier = Modifier.size(width = 84.dp, height = 116.dp),
                shape = RoundedCornerShape(12.dp),
                fallbackIconSize = 36.dp
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.Center
            ) {
                // TÍTULO AMPLIADO (19sp ExtraBold)
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Linha de Badges de alto contraste
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Badge do Ano
                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = movie.year,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Badge de Avaliação
                    if (movie.rating.isNotBlank() && movie.rating != "N/A") {
                        val badgeBg = if (isDark) AmberContainerDark else AmberContainerLight
                        val badgeTextColor = if (isDark) AmberTextDark else AmberTextLight

                        Box(
                            modifier = Modifier
                                .background(color = badgeBg, shape = RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = AmberIcon,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "${movie.rating}/10",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Black,
                                    color = badgeTextColor
                                )
                            }
                        }
                    }
                }
            }

            // Botão de remover com área de toque mínima garantida de 48dp x 48dp
            IconButton(
                onClick = onRemoveClick,
                modifier = Modifier.minimumInteractiveComponentSize()
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Remover dos favoritos",
                    tint = CinemaRuby,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}
