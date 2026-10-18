package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.WorkoutPhase
import com.example.ui.theme.AnimeDarkSurface
import com.example.ui.theme.AnimeGrey
import com.example.ui.theme.AnimeWhite
import com.example.ui.theme.AuraCyan
import com.example.ui.theme.AuraCyanGlow
import com.example.ui.theme.CrimsonRage
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.FlameOrangeGlow
import com.example.ui.theme.KiPurple
import com.example.ui.theme.SuperSaiyanGold

@Composable
fun AnimeKiAuraRing(
    secondsRemaining: Int,
    totalPhaseDuration: Int,
    phase: WorkoutPhase,
    currentRound: Int,
    totalRounds: Int,
    modifier: Modifier = Modifier
) {
    val progress = if (totalPhaseDuration > 0) {
        (secondsRemaining.toFloat() / totalPhaseDuration.toFloat()).coerceIn(0f, 1f)
    } else 0f

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 250, easing = LinearEasing),
        label = "progress"
    )

    // Pulse animation for high energy
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (phase == WorkoutPhase.WORK) 1.04f else 1.01f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (secondsRemaining <= 3) 250 else 700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val primaryAuraColor by animateColorAsState(
        targetValue = when (phase) {
            WorkoutPhase.PREPARE -> KiPurple
            WorkoutPhase.WORK -> if (secondsRemaining <= 3) CrimsonRage else FlameOrange
            WorkoutPhase.REST -> AuraCyan
            WorkoutPhase.COOLDOWN, WorkoutPhase.COMPLETED -> SuperSaiyanGold
        },
        animationSpec = tween(400),
        label = "auraColor"
    )

    val secondaryGlowColor by animateColorAsState(
        targetValue = when (phase) {
            WorkoutPhase.PREPARE -> AuraCyan
            WorkoutPhase.WORK -> SuperSaiyanGold
            WorkoutPhase.REST -> AuraCyanGlow
            WorkoutPhase.COOLDOWN, WorkoutPhase.COMPLETED -> FlameOrangeGlow
        },
        animationSpec = tween(400),
        label = "glowColor"
    )

    Box(
        modifier = modifier
            .size(240.dp)
            .scale(pulseScale),
        contentAlignment = Alignment.Center
    ) {
        // Radial Canvas Ring
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 14.dp.toPx()
            val diameter = size.minDimension - strokeWidth
            val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
            val arcSize = Size(diameter, diameter)

            // Background Track
            drawArc(
                color = Color(0xFF1E2235),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Active Glowing Arc
            val sweepAngle = animatedProgress * 360f
            drawArc(
                brush = Brush.sweepGradient(
                    0.0f to secondaryGlowColor,
                    0.5f to primaryAuraColor,
                    1.0f to secondaryGlowColor
                ),
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        // Inner Content Container
        Column(
            modifier = Modifier
                .size(190.dp)
                .clip(CircleShape)
                .background(AnimeDarkSurface.copy(alpha = 0.92f))
                .border(2.dp, primaryAuraColor.copy(alpha = 0.4f), CircleShape)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Phase Pill Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(primaryAuraColor.copy(alpha = 0.2f))
                    .border(1.dp, primaryAuraColor, RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 3.dp)
            ) {
                Text(
                    text = when (phase) {
                        WorkoutPhase.PREPARE -> "GET READY"
                        WorkoutPhase.WORK -> "FIGHT!"
                        WorkoutPhase.REST -> "RECOVER"
                        WorkoutPhase.COOLDOWN, WorkoutPhase.COMPLETED -> "VICTORY"
                    },
                    color = primaryAuraColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Main Seconds Counter
            Text(
                text = "$secondsRemaining",
                color = AnimeWhite,
                fontSize = 58.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center
            )

            // Round Number Display
            Text(
                text = "ROUND $currentRound / $totalRounds",
                color = if (phase == WorkoutPhase.WORK) SuperSaiyanGold else AnimeGrey,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}
