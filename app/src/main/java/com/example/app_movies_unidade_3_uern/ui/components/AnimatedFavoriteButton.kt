package com.example.app_movies_unidade_3_uern.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Botão animado de favoritar (IconButton) com:
 * - Área de toque mínima garantida de 48dp x 48dp (minimumInteractiveComponentSize)
 * - Escalonamento sutil (bounce/spring scale) no clique
 * - Transição suave com AnimatedContent entre o coração preenchido e a borda
 */
@Composable
fun AnimatedFavoriteButton(
    isFavorite: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = 24.dp,
    activeColor: Color = Color(0xFFE53935)
) {
    var isTriggered by remember { mutableStateOf(false) }

    val bounceScale by animateFloatAsState(
        targetValue = if (isTriggered) 1.35f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        finishedListener = { isTriggered = false },
        label = "favoriteScaleAnim"
    )

    IconButton(
        onClick = {
            isTriggered = true
            onToggle()
        },
        modifier = modifier
            .minimumInteractiveComponentSize()
            .scale(bounceScale)
    ) {
        AnimatedContent(
            targetState = isFavorite,
            transitionSpec = {
                (scaleIn(initialScale = 0.6f) + fadeIn()) togetherWith
                        (scaleOut(targetScale = 0.6f) + fadeOut())
            },
            label = "favoriteIconCrossfade"
        ) { targetIsFavorite ->
            Icon(
                imageVector = if (targetIsFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = if (targetIsFavorite) "Remover dos favoritos" else "Adicionar aos favoritos",
                tint = if (targetIsFavorite) activeColor else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(iconSize)
            )
        }
    }
}

/**
 * FloatingActionButton animado com efeito de escala suave e AnimatedContent no ícone.
 */
@Composable
fun AnimatedFavoriteFab(
    isFavorite: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isTriggered by remember { mutableStateOf(false) }

    val bounceScale by animateFloatAsState(
        targetValue = if (isTriggered) 1.2f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        finishedListener = { isTriggered = false },
        label = "fabScaleAnim"
    )

    FloatingActionButton(
        onClick = {
            isTriggered = true
            onToggle()
        },
        modifier = modifier
            .minimumInteractiveComponentSize()
            .scale(bounceScale),
        containerColor = if (isFavorite) {
            MaterialTheme.colorScheme.errorContainer
        } else {
            MaterialTheme.colorScheme.primaryContainer
        },
        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp)
    ) {
        AnimatedContent(
            targetState = isFavorite,
            transitionSpec = {
                (scaleIn(initialScale = 0.5f) + fadeIn()) togetherWith
                        (scaleOut(targetScale = 0.5f) + fadeOut())
            },
            label = "fabIconAnim"
        ) { targetIsFavorite ->
            Icon(
                imageVector = if (targetIsFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = if (targetIsFavorite) "Remover dos favoritos" else "Adicionar aos favoritos",
                tint = if (targetIsFavorite) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onPrimaryContainer
                },
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
