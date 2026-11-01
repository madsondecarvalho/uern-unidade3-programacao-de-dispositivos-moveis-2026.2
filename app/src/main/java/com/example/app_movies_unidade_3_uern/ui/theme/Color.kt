package com.example.app_movies_unidade_3_uern.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ============================================================================
// Paleta Cinematográfica Harmoniosa (Navy, Crimson & Amber de Alto Contraste)
// Substitui a combinação conflitante roxo/laranja por tons elegantes de cinema
// ============================================================================

// Tons de Destaque Primários (Cinema Blue / Azure)
val CinemaBlue = Color(0xFF2563EB)             // Azul clássico de streaming, enérgico e legível
val CinemaBlueLight = Color(0xFF3B82F6)
val CinemaBlueDark = Color(0xFF60A5FA)
val CinemaBlueContainerLight = Color(0xFFDBEAFE)
val CinemaBlueContainerDark = Color(0xFF1E3A8A)

// Tons Secundários (Cinema Ruby / Crimson para Favoritos e Acentos)
val CinemaRuby = Color(0xFFE11D48)             // Vermelho rubi vibrante
val CinemaRubyLight = Color(0xFFF43F5E)
val CinemaRubyContainerLight = Color(0xFFFFE4E6)
val CinemaRubyContainerDark = Color(0xFF4C0519)

// Tons de Avaliação e Estrelas (Âmbar / Ouro Radiante com ALTO CONTRASTE)
// IMPORTANTE: Nunca usar texto âmbar claro em fundo claro.
val AmberIcon = Color(0xFFFFB800)              // Ouro radiante cinematográfico para estrelas (vibrante e brilhante)
val StarGold = Color(0xFFFFB800)               // Ouro vibrante para estrelas preenchidas
val StarInactive = Color(0xFF94A3B8)           // Contorno nítido e definido para estrelas não preenchidas
val AmberTextLight = Color(0xFF78350F)          // Texto âmbar escuro / bronze para fundo claro (Contraste AAA > 7:1)
val AmberTextDark = Color(0xFFFDE047)           // Texto dourado luminoso para fundo escuro
val AmberContainerLight = Color(0xFFFEF3C7)     // Fundo suave de badge no modo claro
val AmberContainerDark = Color(0xFF382305)      // Fundo suave de badge no modo escuro

// Cores Neutras de Alto Contraste - Tema Claro
val BackgroundLight = Color(0xFFF8FAFC)        // Slate claro suave, não cansativo
val SurfaceLight = Color(0xFFFFFFFF)
val SurfaceVariantLight = Color(0xFFF1F5F9)    // Superfície secundária sutil
val SurfaceContainerLight = Color(0xFFFFFFFF)  // Cards com fundo branco nítido
val SurfaceContainerHighestLight = Color(0xFFE2E8F0)
val TextPrimaryLight = Color(0xFF0F172A)       // Quase preto / Slate 900 (máxima legibilidade)
val TextSecondaryLight = Color(0xFF475569)     // Slate 600 (legibilidade nítida)

// Cores Neutras de Alto Contraste - Tema Escuro (Noite Cinematográfica / Deep Slate)
val BackgroundDark = Color(0xFF0B0F19)         // Azul noite profundo (elegante e moderno)
val SurfaceDark = Color(0xFF111827)            // Superfície escura de cinema
val SurfaceVariantDark = Color(0xFF1F2937)     // Superfície de contraste
val SurfaceContainerDark = Color(0xFF161F30)   // Fundo de cards escuros
val SurfaceContainerHighestDark = Color(0xFF243046)
val TextPrimaryDark = Color(0xFFF8FAFC)        // Slate 50 (off-white cristalino)
val TextSecondaryDark = Color(0xFFCBD5E1)      // Slate 300 (cinza claro suave)

// Animação Shimmer
val ShimmerBaseLight = Color(0xFFE2E8F0)
val ShimmerHighlightLight = Color(0xFFF8FAFC)
val ShimmerBaseDark = Color(0xFF1E293B)
val ShimmerHighlightDark = Color(0xFF334155)

// Gradientes Cinematográficos Elegantes
val HeroCinemaGradient = Brush.horizontalGradient(
    listOf(Color(0xFF1D4ED8), Color(0xFFE11D48))
)

val RankGoldGradient = Brush.horizontalGradient(
    listOf(Color(0xFFD97706), Color(0xFFDC2626))
)

val CardBorderGradient = Brush.horizontalGradient(
    listOf(Color(0xFF3B82F6).copy(alpha = 0.5f), Color(0xFFE11D48).copy(alpha = 0.5f))
)
