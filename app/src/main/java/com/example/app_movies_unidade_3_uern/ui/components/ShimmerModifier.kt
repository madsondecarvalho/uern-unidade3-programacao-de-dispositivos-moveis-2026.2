package com.example.app_movies_unidade_3_uern.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import com.example.app_movies_unidade_3_uern.ui.theme.ShimmerBaseDark
import com.example.app_movies_unidade_3_uern.ui.theme.ShimmerBaseLight
import com.example.app_movies_unidade_3_uern.ui.theme.ShimmerHighlightDark
import com.example.app_movies_unidade_3_uern.ui.theme.ShimmerHighlightLight

/**
 * Modificador para aplicar o efeito Shimmer (Skeleton) com animação contínua
 * da esquerda para a direita, respeitando os temas claro e escuro.
 */
fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition(label = "ShimmerInfiniteTransition")

    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat().coerceAtLeast(1f),
        targetValue = 2 * size.width.toFloat().coerceAtLeast(1f),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1300, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ShimmerOffsetXAnim"
    )

    val isDark = isSystemInDarkTheme()
    val baseColor = if (isDark) ShimmerBaseDark else ShimmerBaseLight
    val highlightColor = if (isDark) ShimmerHighlightDark else ShimmerHighlightLight

    val shimmerColors = listOf(
        baseColor,
        highlightColor,
        baseColor
    )

    this
        .onGloballyPositioned { size = it.size }
        .background(
            brush = Brush.linearGradient(
                colors = shimmerColors,
                start = Offset(startOffsetX, 0f),
                end = Offset(
                    startOffsetX + size.width.toFloat().coerceAtLeast(1f),
                    size.height.toFloat().coerceAtLeast(1f)
                )
            )
        )
}
