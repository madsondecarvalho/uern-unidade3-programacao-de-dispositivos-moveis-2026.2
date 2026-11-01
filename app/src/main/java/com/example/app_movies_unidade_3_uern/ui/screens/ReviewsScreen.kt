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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
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
import androidx.compose.ui.unit.sp
import com.example.app_movies_unidade_3_uern.data.local.MovieReviewEntity
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
import com.example.app_movies_unidade_3_uern.ui.theme.StarGold
import com.example.app_movies_unidade_3_uern.ui.theme.StarInactive
import com.example.app_movies_unidade_3_uern.ui.viewmodel.ReviewsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewsScreen(
    viewModel: ReviewsViewModel,
    onNavigateBack: () -> Unit,
    onMovieClick: (String) -> Unit,
    onExploreMovies: () -> Unit
) {
    val reviewsState by viewModel.reviewsState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Minhas Avaliações",
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
            when (val state = reviewsState) {
                is UiState.Loading -> {
                    MovieListSkeleton()
                }
                is UiState.Success -> {
                    if (state.data.isEmpty()) {
                        EmptyStateView(
                            title = "Você ainda não avaliou nenhum filme",
                            message = "Abra qualquer título no catálogo e toque em 'Avaliar Filme' para registrar suas estrelas e comentário.",
                            icon = Icons.Default.RateReview,
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
                                key = { it.movieId }
                            ) { review ->
                                ReviewCard(
                                    review = review,
                                    onClick = { onMovieClick(review.movieId) },
                                    onDelete = { viewModel.deleteReview(review.movieId) },
                                    modifier = Modifier.animateItem()
                                )
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    ErrorStateView(
                        message = state.message,
                        onRetry = { }
                    )
                }
                is UiState.Idle -> {}
            }
        }
    }
}

@Composable
private fun ReviewCard(
    review: MovieReviewEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }
    val formattedDate = remember(review.timestamp) { dateFormat.format(Date(review.timestamp)) }

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
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Pôster do filme
                MoviePosterView(
                    posterUrl = review.moviePosterUrl,
                    contentDescription = review.movieTitle,
                    modifier = Modifier.size(width = 72.dp, height = 100.dp),
                    shape = RoundedCornerShape(10.dp),
                    fallbackIconSize = 32.dp
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    // Título do filme
                    Text(
                        text = review.movieTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Ano: ${review.movieYear}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Linha de Estrelas com badge
                    val badgeBg = if (isDark) StarGold.copy(alpha = 0.2f) else StarGold.copy(alpha = 0.15f)
                    val badgeTextColor = if (isDark) AmberTextDark else AmberTextLight

                    Box(
                        modifier = Modifier
                            .background(color = badgeBg, shape = RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            for (i in 1..5) {
                                Icon(
                                    imageVector = if (i <= review.rating) Icons.Default.Star else Icons.Outlined.StarBorder,
                                    contentDescription = null,
                                    tint = if (i <= review.rating) StarGold else StarInactive.copy(alpha = 0.45f),
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${review.rating}/5",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Black,
                                color = badgeTextColor
                            )
                        }
                    }
                }

                // Botão de deletar avaliação
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .size(38.dp)
                        .background(
                            color = CinemaRuby.copy(alpha = 0.12f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Excluir avaliação",
                        tint = CinemaRuby,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Exibição do comentário do usuário com barra lateral dourada
            if (review.comment.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .height(20.dp)
                            .background(StarGold, shape = RoundedCornerShape(2.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "“${review.comment}”",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Data de registro
            Text(
                text = "Avaliado em $formattedDate",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}
