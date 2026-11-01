package com.example.app_movies_unidade_3_uern.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_movies_unidade_3_uern.data.local.MovieEntity
import com.example.app_movies_unidade_3_uern.data.local.MovieReviewEntity
import com.example.app_movies_unidade_3_uern.ui.components.AnimatedFavoriteButton
import com.example.app_movies_unidade_3_uern.ui.components.AnimatedFavoriteFab
import com.example.app_movies_unidade_3_uern.ui.components.ErrorStateView
import com.example.app_movies_unidade_3_uern.ui.components.MovieDetailsSkeleton
import com.example.app_movies_unidade_3_uern.ui.components.MoviePosterView
import com.example.app_movies_unidade_3_uern.ui.components.RatingReviewDialog
import com.example.app_movies_unidade_3_uern.ui.state.UiState
import com.example.app_movies_unidade_3_uern.ui.theme.AmberContainerDark
import com.example.app_movies_unidade_3_uern.ui.theme.AmberContainerLight
import com.example.app_movies_unidade_3_uern.ui.theme.AmberIcon
import com.example.app_movies_unidade_3_uern.ui.theme.AmberTextDark
import com.example.app_movies_unidade_3_uern.ui.theme.AmberTextLight
import com.example.app_movies_unidade_3_uern.ui.theme.CinemaRuby
import com.example.app_movies_unidade_3_uern.ui.theme.StarGold
import com.example.app_movies_unidade_3_uern.ui.theme.StarInactive
import com.example.app_movies_unidade_3_uern.ui.viewmodel.DetailsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    movieId: String,
    viewModel: DetailsViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToReviews: () -> Unit = {}
) {
    LaunchedEffect(movieId) {
        viewModel.loadMovieDetails(movieId)
    }

    val detailsState by viewModel.detailsState.collectAsState()
    val userReview by viewModel.userReview.collectAsState()
    val currentMovie = (detailsState as? UiState.Success)?.data
    val snackbarHostState = remember { SnackbarHostState() }
    val isDark = isSystemInDarkTheme()

    var showRatingModal by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = currentMovie?.title ?: "Detalhes do Filme",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
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
                actions = {
                    IconButton(
                        onClick = onNavigateToReviews,
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.RateReview,
                            contentDescription = "Minhas Avaliações",
                            tint = AmberIcon
                        )
                    }
                    if (currentMovie != null) {
                        AnimatedFavoriteButton(
                            isFavorite = currentMovie.isFavorite,
                            onToggle = { viewModel.toggleFavorite(currentMovie) }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            if (currentMovie != null) {
                AnimatedFavoriteFab(
                    isFavorite = currentMovie.isFavorite,
                    onToggle = { viewModel.toggleFavorite(currentMovie) }
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = detailsState) {
                is UiState.Loading -> {
                    MovieDetailsSkeleton()
                }
                is UiState.Success -> {
                    MovieDetailsContent(
                        movie = state.data,
                        userReview = userReview,
                        isDark = isDark,
                        onOpenRatingDialog = { showRatingModal = true },
                        onDeleteReview = { viewModel.deleteReview(state.data.id) },
                        onNavigateToReviews = onNavigateToReviews
                    )
                }
                is UiState.Error -> {
                    ErrorStateView(
                        message = state.message,
                        onRetry = { viewModel.loadMovieDetails(movieId) }
                    )
                }
                is UiState.Idle -> {}
            }

            // Modal / Dialog interativo de avaliação (1 a 5 estrelas e comentário)
            if (showRatingModal && currentMovie != null) {
                RatingReviewDialog(
                    movieTitle = currentMovie.title,
                    initialRating = userReview?.rating ?: 0,
                    initialComment = userReview?.comment ?: "",
                    onDismiss = { showRatingModal = false },
                    onSave = { rating, comment ->
                        viewModel.saveReview(currentMovie, rating, comment)
                        showRatingModal = false
                    }
                )
            }
        }
    }
}

@Composable
private fun MovieDetailsContent(
    movie: MovieEntity,
    userReview: MovieReviewEntity?,
    isDark: Boolean,
    onOpenRatingDialog: () -> Unit,
    onDeleteReview: () -> Unit,
    onNavigateToReviews: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Pôster grande com Coil, Shimmer e borda refinada
        Card(
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            MoviePosterView(
                posterUrl = movie.posterUrl,
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth(0.72f)
                    .height(360.dp),
                shape = RoundedCornerShape(20.dp),
                fallbackIconSize = 64.dp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // TÍTULO DO FILME AMPLIADO (24sp Black)
        Text(
            text = movie.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Linha com Ano e Avaliação IMDb com ALTO CONTRASTE
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Pílula do Ano
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Ano: ${movie.year}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Pílula da Avaliação IMDb
            if (movie.rating.isNotBlank() && movie.rating != "N/A") {
                val badgeBg = if (isDark) AmberContainerDark else AmberContainerLight
                val badgeTextColor = if (isDark) AmberTextDark else AmberTextLight

                Box(
                    modifier = Modifier
                        .background(color = badgeBg, shape = RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Avaliação",
                            tint = AmberIcon,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${movie.rating} / 10 IMDb",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Black,
                            color = badgeTextColor
                        )
                    }
                }
            }
        }

        // Chips de Gênero
        if (movie.genre.isNotBlank() && movie.genre != "N/A") {
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                movie.genre.split(",").take(3).forEach { genreItem ->
                    SuggestionChip(
                        onClick = { },
                        label = {
                            Text(
                                text = genreItem.trim(),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            labelColor = MaterialTheme.colorScheme.onSurface
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // SEÇÃO: BOTÃO OU CARD DE AVALIAÇÃO DO USUÁRIO
        if (userReview == null) {
            Button(
                onClick = onOpenRatingDialog,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = AmberIcon,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Avaliar Filme",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        } else {
            UserReviewCard(
                review = userReview,
                isDark = isDark,
                onEdit = onOpenRatingDialog,
                onDelete = onDeleteReview,
                onNavigateToReviews = onNavigateToReviews
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Card da Sinopse (Plot)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Sinopse",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = if (movie.plot.isNotBlank() && movie.plot != "N/A") movie.plot else "Sinopse não disponível.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Indicador permanente Offline-First
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 14.dp, vertical = 7.dp)
        ) {
            Text(
                text = "⚡ Disponível Offline • Acesso Permanente",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }

        Spacer(modifier = Modifier.height(80.dp)) // Espaço para não ser coberto pelo FloatingActionButton
    }
}

/**
 * Card exibindo a avaliação do usuário com layout cinematográfico harmonioso,
 * estrelas radiantes e disposição refinada dos elementos.
 */
@Composable
private fun UserReviewCard(
    review: MovieReviewEntity,
    isDark: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onNavigateToReviews: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy 'às' HH:mm", Locale.getDefault()) }
    val formattedDate = remember(review.timestamp) { dateFormat.format(Date(review.timestamp)) }

    val ratingLabel = when (review.rating) {
        1 -> "Péssimo"
        2 -> "Ruim"
        3 -> "Regular"
        4 -> "Muito Bom"
        5 -> "Obra-Prima!"
        else -> ""
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        border = BorderStroke(
            width = 1.5.dp,
            color = StarGold.copy(alpha = if (isDark) 0.45f else 0.35f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // 1. Cabeçalho com Badge "SUA AVALIAÇÃO" e Ações (Editar e Excluir)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Badge dourado elegante com ícone de estrela
                Box(
                    modifier = Modifier
                        .background(
                            color = StarGold.copy(alpha = if (isDark) 0.22f else 0.16f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = StarGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "SUA AVALIAÇÃO",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.8.sp,
                            color = if (isDark) AmberTextDark else AmberTextLight
                        )
                    }
                }

                // Botões de Ação refinados e circulares (touch target >= 48dp)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .size(38.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar avaliação",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

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
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Linha das Estrelas com Pontuação Grande e Rótulo
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 5 Estrelas radiantes com ouro vivo
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (i in 1..5) {
                        val isFilled = i <= review.rating
                        Icon(
                            imageVector = if (isFilled) Icons.Default.Star else Icons.Outlined.StarBorder,
                            contentDescription = null,
                            tint = if (isFilled) StarGold else StarInactive.copy(alpha = 0.45f),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Nota (ex: 5/5) e Rótulo (ex: Obra-Prima!)
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "${review.rating}/5",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (ratingLabel.isNotBlank()) {
                        Text(
                            text = "• $ratingLabel",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) AmberTextDark else AmberTextLight,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }
            }

            // 3. Comentário / Resenha com Barra Lateral Dourada
            if (review.comment.isNotBlank()) {
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .height(26.dp)
                            .background(StarGold, shape = RoundedCornerShape(2.dp))
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "“${review.comment}”",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Normal,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4. Divisor sutil e Rodapé
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Registrado em $formattedDate",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )

                TextButton(
                    onClick = onNavigateToReviews,
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Text(
                        text = "Ver todas →",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
