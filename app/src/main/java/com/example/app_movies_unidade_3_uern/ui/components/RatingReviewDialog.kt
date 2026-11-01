package com.example.app_movies_unidade_3_uern.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_movies_unidade_3_uern.ui.theme.AmberTextDark
import com.example.app_movies_unidade_3_uern.ui.theme.AmberTextLight
import com.example.app_movies_unidade_3_uern.ui.theme.StarGold
import com.example.app_movies_unidade_3_uern.ui.theme.StarInactive

/**
 * Modal (Dialog) interativo para avaliação de filme pelo usuário:
 * - Seletor de estrelas com animação tátil arrastando para esquerda ou direita (drag-to-rate)
 * - Estrelas em ouro vibrante de alto contraste e realce visual
 * - Campo de texto com suporte pleno ao teclado e caracteres especiais brasileiros (ã, ç, é, etc.)
 * - Botões limpos e diretos ("Salvar" e "Cancelar")
 */
@Composable
fun RatingReviewDialog(
    movieTitle: String,
    initialRating: Int = 0,
    initialComment: String = "",
    onDismiss: () -> Unit,
    onSave: (rating: Int, comment: String) -> Unit
) {
    var rating by remember { mutableIntStateOf(initialRating.coerceIn(0, 5)) }
    var comment by remember { mutableStateOf(initialComment) }
    val isDark = isSystemInDarkTheme()

    val ratingLabels = mapOf(
        0 to "Arraste ou toque nas estrelas para avaliar",
        1 to "★☆☆☆☆ - Péssimo",
        2 to "★★☆☆☆ - Ruim",
        3 to "★★★☆☆ - Regular",
        4 to "★★★★☆ - Muito Bom",
        5 to "★★★★★ - Obra-Prima!"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = "Avaliar Filme",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = movieTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Barra de estrelas interativa com animação de arrasto para a direita/esquerda
                InteractiveStarRatingBar(
                    rating = rating,
                    onRatingChanged = { rating = it },
                    isDark = isDark
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Descrição textual da nota
                Text(
                    text = ratingLabels[rating] ?: "",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (rating > 0) {
                        if (isDark) AmberTextDark else AmberTextLight
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Campo para tecer comentário sobre o filme - aceita acentuação brasileira (ã, ç, é, etc.)
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Seu comentário sobre o filme") },
                    placeholder = { Text("Ex: Excelente atuação, direção primorosa e ótima história...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 120.dp),
                    shape = RoundedCornerShape(16.dp),
                    minLines = 3,
                    maxLines = 5,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        autoCorrectEnabled = true,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Default
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(rating, comment.trim()) },
                enabled = rating > 0,
                modifier = Modifier.heightIn(min = 44.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Salvar",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.heightIn(min = 44.dp)
            ) {
                Text(
                    text = "Cancelar",
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = MaterialTheme.colorScheme.surface
    )
}

/**
 * Componente que renderiza 5 estrelas vibrantes com animação contínua
 * ao arrastar o dedo para a esquerda ou para a direita, além do clique individual.
 */
@Composable
private fun InteractiveStarRatingBar(
    rating: Int,
    onRatingChanged: (Int) -> Unit,
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    var totalWidth by remember { mutableFloatStateOf(0f) }

    fun updateRatingFromPosition(touchX: Float) {
        if (totalWidth > 0f) {
            val fraction = (touchX / totalWidth).coerceIn(0f, 1f)
            val calculated = ((fraction * 5).toInt() + 1).coerceIn(1, 5)
            onRatingChanged(calculated)
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Área sensível ao toque e ao arrasto horizontal contínuo (esquerda <-> direita)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .onSizeChanged { totalWidth = it.width.toFloat() }
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        updateRatingFromPosition(down.position.x)

                        while (true) {
                            val event = awaitPointerEvent()
                            val change = event.changes.firstOrNull() ?: break
                            if (!change.pressed) break
                            updateRatingFromPosition(change.position.x)
                            change.consume()
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                for (star in 1..5) {
                    val isSelected = star <= rating
                    val isCurrentTip = star == rating

                    // Animação de escala com mola elástica (spring) para destaque ao arrastar
                    val scale by animateFloatAsState(
                        targetValue = when {
                            isCurrentTip -> 1.25f
                            isSelected -> 1.08f
                            else -> 0.95f
                        },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        label = "star_scale_$star"
                    )

                    // Transição de cor viva: ouro radiante quando selecionado, contorno nítido quando não
                    val starColor by animateColorAsState(
                        targetValue = if (isSelected) StarGold else StarInactive,
                        animationSpec = tween(durationMillis = 150),
                        label = "star_color_$star"
                    )

                    val containerBg = if (isSelected) {
                        StarGold.copy(alpha = 0.18f)
                    } else {
                        if (isDark) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
                    }

                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                            }
                            .background(color = containerBg, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isSelected) Icons.Default.Star else Icons.Outlined.StarBorder,
                            contentDescription = "$star estrela(s)",
                            tint = starColor,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Barra de progresso suave indicando o preenchimento proporcional (arrasto animado)
        val animatedProgress by animateFloatAsState(
            targetValue = (rating / 5f).coerceIn(0f, 1f),
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMediumLow
            ),
            label = "rating_progress_bar"
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .height(4.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(2.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .height(4.dp)
                    .background(
                        color = StarGold,
                        shape = RoundedCornerShape(2.dp)
                    )
            )
        }
    }
}
