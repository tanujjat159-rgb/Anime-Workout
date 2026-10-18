package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AnimeQuote
import com.example.model.WorkoutPhase
import com.example.ui.theme.AnimeDarkSurface
import com.example.ui.theme.AnimeGrey
import com.example.ui.theme.AnimeOnyx
import com.example.ui.theme.AnimeWhite
import com.example.ui.theme.AuraCyan
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.SuperSaiyanGold

@Composable
fun AnimeFightCard(
    currentPhase: WorkoutPhase,
    currentQuote: AnimeQuote?,
    onReplayQuoteAudio: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val imageRes = when (currentPhase) {
        WorkoutPhase.PREPARE -> R.drawable.img_power_recovery
        WorkoutPhase.WORK -> R.drawable.img_fight_battle
        WorkoutPhase.REST -> R.drawable.img_power_recovery
        WorkoutPhase.COOLDOWN, WorkoutPhase.COMPLETED -> R.drawable.img_limit_break
    }

    val accentColor = when (currentPhase) {
        WorkoutPhase.WORK -> FlameOrange
        WorkoutPhase.REST -> AuraCyan
        WorkoutPhase.PREPARE -> SuperSaiyanGold
        WorkoutPhase.COOLDOWN, WorkoutPhase.COMPLETED -> SuperSaiyanGold
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(230.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(1.5.dp, accentColor.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
    ) {
        // Battle scene image
        AnimatedContent(
            targetState = imageRes,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "fightSceneAnim"
        ) { targetRes ->
            Image(
                painter = painterResource(id = targetRes),
                contentDescription = "Anime Battle Scene",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Shonen Vignette Gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.0f to Color.Transparent,
                        0.45f to AnimeOnyx.copy(alpha = 0.6f),
                        0.85f to AnimeDarkSurface.copy(alpha = 0.96f)
                    )
                )
        )

        // Anime Quote Overlay
        if (currentQuote != null) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                // Character & Series Tag
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(accentColor)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = currentQuote.character.uppercase(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = AnimeOnyx
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "• ${currentQuote.anime}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SuperSaiyanGold
                        )
                    }

                    // Audio Speak Button
                    IconButton(
                        onClick = { onReplayQuoteAudio(currentQuote.quote) },
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("speak_quote_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Campaign,
                            contentDescription = "Play Quote Audio",
                            tint = accentColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // The Motivational Quote
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.FormatQuote,
                        contentDescription = null,
                        tint = accentColor.copy(alpha = 0.7f),
                        modifier = Modifier
                            .size(16.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "\"${currentQuote.quote}\"",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        color = AnimeWhite,
                        lineHeight = 18.sp,
                        maxLines = 3
                    )
                }

                if (!currentQuote.japaneseRomanji.isNullOrBlank()) {
                    Text(
                        text = "🇯🇵 ${currentQuote.japaneseRomanji}",
                        fontSize = 10.sp,
                        color = AnimeGrey,
                        modifier = Modifier.padding(start = 20.dp, top = 2.dp)
                    )
                }
            }
        }
    }
}
