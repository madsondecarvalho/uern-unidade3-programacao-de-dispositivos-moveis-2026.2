package com.example.app_movies_unidade_3_uern.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

/**
 * Componente otimizado e confiável para exibição de pôsteres com Coil:
 * - Normaliza URLs HTTP para HTTPS (evita bloqueios de cleartext no Android 9+)
 * - Utiliza AsyncImage de alto desempenho (ideal para LazyColumn, sem o overhead de subcomposição)
 * - Exibe shimmer contínuo enquanto carrega
 * - Transição com crossfade suave ao carregar
 * - Exibe fallback amigável caso a imagem não exista ou falhe
 */
@Composable
fun MoviePosterView(
    posterUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.dp),
    fallbackIconSize: Dp = 36.dp
) {
    val cleanUrl = remember(posterUrl) {
        val trimmed = posterUrl?.trim()
        if (trimmed.isNullOrBlank() || trimmed == "N/A") {
            null
        } else if (trimmed.startsWith("http://", ignoreCase = true)) {
            trimmed.replaceFirst("http://", "https://", ignoreCase = true)
        } else {
            trimmed
        }
    }

    if (cleanUrl != null) {
        val context = LocalContext.current
        val imageRequest = remember(cleanUrl) {
            ImageRequest.Builder(context)
                .data(cleanUrl)
                .crossfade(true)
                .crossfade(300)
                .build()
        }

        var isImageLoading by remember(cleanUrl) { mutableStateOf(true) }
        var hasImageError by remember(cleanUrl) { mutableStateOf(false) }

        Box(
            modifier = modifier.clip(shape),
            contentAlignment = Alignment.Center
        ) {
            if (!hasImageError) {
                AsyncImage(
                    model = imageRequest,
                    contentDescription = contentDescription,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    onLoading = {
                        isImageLoading = true
                        hasImageError = false
                    },
                    onSuccess = {
                        isImageLoading = false
                        hasImageError = false
                    },
                    onError = {
                        isImageLoading = false
                        hasImageError = true
                    }
                )
            }

            // Exibe efeito Shimmer enquanto a imagem é baixada pela rede
            if (isImageLoading && !hasImageError) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .shimmerEffect()
                )
            }

            // Fallback caso a imagem não possa ser carregada
            if (hasImageError) {
                FallbackPosterView(
                    modifier = Modifier.fillMaxSize(),
                    shape = shape,
                    iconSize = fallbackIconSize
                )
            }
        }
    } else {
        FallbackPosterView(
            modifier = modifier,
            shape = shape,
            iconSize = fallbackIconSize
        )
    }
}

@Composable
private fun FallbackPosterView(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.dp),
    iconSize: Dp = 36.dp
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Movie,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(iconSize)
        )
    }
}
