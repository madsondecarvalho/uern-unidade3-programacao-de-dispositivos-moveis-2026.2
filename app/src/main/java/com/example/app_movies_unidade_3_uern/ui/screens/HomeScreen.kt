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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_movies_unidade_3_uern.ui.theme.AmberContainerDark
import com.example.app_movies_unidade_3_uern.ui.theme.AmberContainerLight
import com.example.app_movies_unidade_3_uern.ui.theme.AmberIcon
import com.example.app_movies_unidade_3_uern.ui.theme.AmberTextDark
import com.example.app_movies_unidade_3_uern.ui.theme.AmberTextLight
import com.example.app_movies_unidade_3_uern.ui.theme.CinemaBlue
import com.example.app_movies_unidade_3_uern.ui.theme.CinemaBlueContainerDark
import com.example.app_movies_unidade_3_uern.ui.theme.CinemaBlueContainerLight
import com.example.app_movies_unidade_3_uern.ui.theme.CinemaBlueDark
import com.example.app_movies_unidade_3_uern.ui.theme.CinemaRuby
import com.example.app_movies_unidade_3_uern.ui.theme.CinemaRubyContainerDark
import com.example.app_movies_unidade_3_uern.ui.theme.CinemaRubyContainerLight
import com.example.app_movies_unidade_3_uern.ui.theme.CinemaRubyLight
import com.example.app_movies_unidade_3_uern.ui.theme.HeroCinemaGradient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToMovieList: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToReviews: () -> Unit
) {
    val isDark = isSystemInDarkTheme()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "CineApp UERN",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // Ícone Hero com gradiente cinematográfico harmonioso
            Box(
                modifier = Modifier
                    .size(92.dp)
                    .background(HeroCinemaGradient, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Movie,
                    contentDescription = "Ícone de Filmes",
                    tint = Color.White,
                    modifier = Modifier.size(50.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Título de boas-vindas
            Text(
                text = "CineApp OMDb",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtítulo com a premissa Offline-First
            Text(
                text = "Modo Offline Inteligente: conforme você navega pelo catálogo, seus filmes, favoritos e avaliações ficam salvos no seu dispositivo para acesso permanente mesmo sem internet.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(26.dp))

            // Opção 1: Explorar Filmes & Top 10
            MenuOptionCard(
                title = "Explorar Catálogo & Top 10",
                subtitle = "Top 10 obras-primas e busca completa integrada",
                tagText = "⭐ Top 10 IMDb",
                tagBgColor = if (isDark) CinemaBlueContainerDark else CinemaBlueContainerLight,
                tagTextColor = if (isDark) CinemaBlueDark else CinemaBlue,
                icon = Icons.Default.Search,
                iconTint = MaterialTheme.colorScheme.primary,
                onClick = onNavigateToMovieList
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Opção 2: Meus Favoritos
            MenuOptionCard(
                title = "Meus Filmes Favoritos",
                subtitle = "Títulos marcados com coração salvos no dispositivo",
                tagText = "❤️ Favoritos",
                tagBgColor = if (isDark) CinemaRubyContainerDark else CinemaRubyContainerLight,
                tagTextColor = if (isDark) CinemaRubyLight else CinemaRuby,
                icon = Icons.Default.Favorite,
                iconTint = MaterialTheme.colorScheme.tertiary,
                onClick = onNavigateToFavorites
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Opção 3: Minhas Avaliações
            MenuOptionCard(
                title = "Minhas Avaliações",
                subtitle = "Notas (1 a 5 estrelas) e resenhas registradas por você",
                tagText = "⭐ Avaliações",
                tagBgColor = if (isDark) AmberContainerDark else AmberContainerLight,
                tagTextColor = if (isDark) AmberTextDark else AmberTextLight,
                icon = Icons.Default.RateReview,
                iconTint = AmberIcon,
                onClick = onNavigateToReviews
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun MenuOptionCard(
    title: String,
    subtitle: String,
    tagText: String,
    tagBgColor: Color,
    tagTextColor: Color,
    icon: ImageVector,
    iconTint: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 108.dp)
            .minimumInteractiveComponentSize(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        color = iconTint.copy(alpha = 0.14f),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .background(tagBgColor, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = tagText,
                        color = tagTextColor,
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navegar",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
