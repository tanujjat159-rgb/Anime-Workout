package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.SuperSaiyanGold
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnimeSpeedlinesCanvas(
    modifier: Modifier = Modifier,
    isActive: Boolean = true,
    accentColor: Color = FlameOrange
) {
    if (!isActive) return

    val infiniteTransition = rememberInfiniteTransition(label = "speedlines")
    val phaseOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val maxRadius = kotlin.math.max(size.width, size.height) * 0.75f
        val innerRadius = 130f

        val lineCount = 36
        for (i in 0 until lineCount) {
            val angle = (i * (360f / lineCount) + (phaseOffset * 15f)) * (Math.PI.toFloat() / 180f)
            val strokeMultiplier = if (i % 2 == 0) 1.6f else 0.8f
            val startR = innerRadius + (i % 5) * 20f + (phaseOffset * 30f)
            val endR = maxRadius

            val startX = centerX + startR * cos(angle)
            val startY = centerY + startR * sin(angle)
            val endX = centerX + endR * cos(angle)
            val endY = centerY + endR * sin(angle)

            val lineColor = if (i % 3 == 0) SuperSaiyanGold.copy(alpha = 0.28f) else accentColor.copy(alpha = 0.18f)

            drawLine(
                color = lineColor,
                start = Offset(startX, startY),
                end = Offset(endX, endY),
                strokeWidth = (2.5f * strokeMultiplier),
                cap = StrokeCap.Round
            )
        }
    }
}
