package com.example.app_movies_unidade_3_uern.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_movies_unidade_3_uern.data.remote.dto.MovieSummaryDto
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
import com.example.app_movies_unidade_3_uern.ui.theme.CinemaBlue
import com.example.app_movies_unidade_3_uern.ui.theme.CinemaBlueContainerDark
import com.example.app_movies_unidade_3_uern.ui.theme.CinemaBlueContainerLight
import com.example.app_movies_unidade_3_uern.ui.theme.CinemaBlueDark
import com.example.app_movies_unidade_3_uern.ui.theme.RankGoldGradient
import com.example.app_movies_unidade_3_uern.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(
    viewModel: HomeViewModel,
    onNavigateBack: () -> Unit,
    onMovieClick: (String) -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val moviesState by viewModel.moviesState.collectAsState()
    val isShowingTopRated by viewModel.isShowingTopRated.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(moviesState) {
        if (moviesState is UiState.Error) {
            val errorMsg = (moviesState as UiState.Error).message
            snackbarHostState.showSnackbar(errorMsg)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isShowingTopRated) "⭐ Top 10 Obras-Primas" else "Catálogo de Filmes",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Banner proativo avisando que o dispositivo está sem internet e não pode pesquisar novos filmes
            if (!isOnline) {
                OfflineNoticeBanner()
            }

            // Barra de busca com visual cinematográfico e resposta a offline
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onQueryChanged(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = {
                    Text(
                        text = if (isOnline) "Pesquise por filme (ex: Matrix, Batman...)" else "Sem internet • Pesquisas pausadas",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                leadingIcon = {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                viewModel.onQueryChanged("")
                                focusManager.clearFocus()
                            },
                            modifier = Modifier.minimumInteractiveComponentSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Limpar busca",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focusManager.clearFocus()
                        viewModel.searchMovies()
                    }
                )
            )

            // Chips rápidos com visual refinado
            QuickFilterChips(
                isShowingTopRated = isShowingTopRated,
                onSelectChip = { query ->
                    if (query == "TOP_10") {
                        viewModel.loadTopRatedMovies()
                    } else {
                        viewModel.onQueryChanged(query)
                        viewModel.searchMovies(query)
                    }
                }
            )

            // Banner explicativo do Top 10 com alto contraste
            if (isShowingTopRated) {
                TopRatedBanner()
            }

            // Conteúdo dinâmico com tratamento de Estados
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when (val state = moviesState) {
                    is UiState.Loading -> {
                        MovieListSkeleton()
                    }
                    is UiState.Success -> {
                        if (state.data.isEmpty()) {
                            EmptyStateView(
                                title = "Nenhum filme encontrado",
                                message = "Não encontramos títulos para '$searchQuery'. Experimente outras palavras-chave ou explore os mais bem avaliados.",
                                icon = Icons.Default.Search,
                                actionButtonText = "Voltar ao Top 10",
                                onActionButtonClick = { viewModel.loadTopRatedMovies() }
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(14.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
                            ) {
                                itemsIndexed(
                                    items = state.data,
                                    key = { _, item -> item.imdbId }
                                ) { index, movie ->
                                    MovieSummaryCard(
                                        movie = movie,
                                        rank = if (isShowingTopRated) index + 1 else null,
                                        onClick = { onMovieClick(movie.imdbId) },
                                        modifier = Modifier.animateItem()
                                    )
                                }
                            }
                        }
                    }
                    is UiState.Error -> {
                        // Tela de erro altamente personalizada com detecção de falta de internet
                        ErrorStateView(
                            message = state.message,
                            onRetry = { viewModel.searchMovies() },
                            onNavigateToSaved = { viewModel.loadTopRatedMovies() }
                        )
                    }
                    is UiState.Idle -> {
                        EmptyStateView(
                            title = "Comece sua pesquisa",
                            message = "Digite o título de um filme acima para consultar o catálogo.",
                            icon = Icons.Default.Search
                        )
                    }
                }
            }
        }
    }
}

/**
 * Banner proativo de aviso de modo offline.
 * Informa ao usuário que novas buscas exigem conexão, enquanto os dados locais continuam acessíveis.
 */
@Composable
private fun OfflineNoticeBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(MaterialTheme.colorScheme.error.copy(alpha = 0.16f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.WifiOff,
                    contentDescription = "Offline",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Você está offline",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.error
                )
                Text(
                    text = "Novas pesquisas estão pausadas até você se reconectar. Seus filmes, favoritos e avaliações salvos no dispositivo continuam disponíveis!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

/**
 * Banner apresentando o Top 10 com texto de alto contraste.
 */
@Composable
private fun TopRatedBanner() {
    val isDark = isSystemInDarkTheme()
    val bannerTitleColor = if (isDark) AmberTextDark else AmberTextLight

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) AmberContainerDark.copy(alpha = 0.5f) else AmberContainerLight.copy(alpha = 0.6f)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isDark) AmberIcon.copy(alpha = 0.4f) else AmberIcon.copy(alpha = 0.35f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(RankGoldGradient, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Whatshot,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "TOP 10 MAIS BEM AVALIADOS",
                    style = MaterialTheme.typography.labelLarge,
                    color = bannerTitleColor,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "As maiores obras-primas da história segundo o IMDb",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Chips de filtro com alta legibilidade e contraste.
 */
@Composable
private fun QuickFilterChips(
    isShowingTopRated: Boolean,
    onSelectChip: (String) -> Unit
) {
    val quickTopics = listOf("Batman", "Matrix", "Lord of the Rings", "Inception", "Avengers")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AssistChip(
            onClick = { onSelectChip("TOP_10") },
            label = {
                Text(
                    text = "⭐ Top 10 IMDb",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            },
            colors = AssistChipDefaults.assistChipColors(
                containerColor = if (isShowingTopRated) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                labelColor = if (isShowingTopRated) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
            ),
            border = if (isShowingTopRated) BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null
        )

        quickTopics.forEach { topic ->
            AssistChip(
                onClick = { onSelectChip(topic) },
                label = {
                    Text(
                        text = topic,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    labelColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}

/**
 * Card horizontal de filme ampliado:
 * - Altura mínima garantida de 132dp
 * - Título em 19sp ExtraBold (alta legibilidade)
 * - Badges com cores contrastantes
 */
@Composable
fun MovieSummaryCard(
    movie: MovieSummaryDto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    rank: Int? = null
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
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 132.dp)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Pôster ampliado (84dp x 116dp)
            Box {
                MoviePosterView(
                    posterUrl = movie.poster,
                    contentDescription = movie.title,
                    modifier = Modifier.size(width = 84.dp, height = 116.dp),
                    shape = RoundedCornerShape(12.dp),
                    fallbackIconSize = 36.dp
                )

                // Selo de Rank se for do Top 10 (fundo gradiente e texto branco legível)
                if (rank != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(4.dp)
                            .background(RankGoldGradient, RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "#$rank",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.Center
            ) {
                // TÍTULO AMPLIADO (19sp ExtraBold) com alto contraste
                Text(
                    text = movie.title ?: "Sem título",
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
                            text = movie.year ?: "N/A",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Se tiver avaliação ou tipo, exibir com contraste adequado
                    if (!movie.type.isNullOrBlank()) {
                        val isRating = movie.type.contains("★") || movie.type.contains(".")

                        val badgeBg = if (isRating) {
                            if (isDark) AmberContainerDark else AmberContainerLight
                        } else {
                            if (isDark) CinemaBlueContainerDark else CinemaBlueContainerLight
                        }

                        val badgeTextColor = if (isRating) {
                            if (isDark) AmberTextDark else AmberTextLight
                        } else {
                            if (isDark) CinemaBlueDark else CinemaBlue
                        }

                        Box(
                            modifier = Modifier
                                .background(color = badgeBg, shape = RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (isRating) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = AmberIcon,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                }
                                Text(
                                    text = movie.type.replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.labelMedium,
                                    color = badgeTextColor,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
