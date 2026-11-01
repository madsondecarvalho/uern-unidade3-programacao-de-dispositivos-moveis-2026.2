package com.example.app_movies_unidade_3_uern.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Componente altamente personalizado para exibição de erros com diagnóstico inteligente:
 * - Detecta erros de conexão / offline e exibe orientações claras sobre o modo Offline-First
 * - Detecta erros de filmes não encontrados
 * - Permite ações secundárias como navegar para itens salvos
 */
@Composable
fun ErrorStateView(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToSaved: (() -> Unit)? = null
) {
    val isConnectionError = message.contains("conexão", ignoreCase = true) ||
            message.contains("offline", ignoreCase = true) ||
            message.contains("network", ignoreCase = true) ||
            message.contains("timeout", ignoreCase = true) ||
            message.contains("internet", ignoreCase = true) ||
            message.contains("Failed to connect", ignoreCase = true) ||
            message.contains("Unable to resolve host", ignoreCase = true)

    val isNotFoundError = message.contains("não encontrado", ignoreCase = true) ||
            message.contains("not found", ignoreCase = true) ||
            message.contains("nenhum filme", ignoreCase = true)

    val icon: ImageVector = when {
        isConnectionError -> Icons.Outlined.WifiOff
        isNotFoundError -> Icons.Outlined.SearchOff
        else -> Icons.Outlined.ErrorOutline
    }

    val title: String = when {
        isConnectionError -> "Sem Conexão com a Internet"
        isNotFoundError -> "Filme Não Encontrado"
        else -> "Ops! Ocorreu um Problema"
    }

    val descriptiveMessage: String = when {
        isConnectionError -> "Você está sem conexão com a internet. O aplicativo opera no modo Offline-First, permitindo acessar os filmes, favoritos e avaliações já salvos no seu dispositivo, mas novas buscas exigem conexão ativa."
        isNotFoundError -> message
        else -> message
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Círculo com ícone estilizado
        Box(
            modifier = Modifier
                .size(86.dp)
                .background(
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.65f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(44.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Título estilizado
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Mensagem detalhada com alto contraste
        Text(
            text = descriptiveMessage,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            modifier = Modifier.fillMaxWidth(0.92f)
        )

        // Se for erro de conexão, exibe card explicativo do modo Offline-First
        if (isConnectionError) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(0.92f),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CloudOff,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Dica: Títulos já abertos e suas avaliações continuam 100% disponíveis localmente no dispositivo.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(26.dp))

        // Botão de retry com touch target >= 48dp
        Button(
            onClick = onRetry,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .heightIn(min = 50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isConnectionError) "Verificar Conexão" else "Tentar Novamente",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }

        // Ação secundária opcional
        if (onNavigateToSaved != null && isConnectionError) {
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = onNavigateToSaved,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .heightIn(min = 46.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Acessar Meus Dados Salvos",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
