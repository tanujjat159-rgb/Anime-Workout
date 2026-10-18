package com.example.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.timer.WorkoutTimerUiState
import com.example.ui.theme.AnimeCardBorder
import com.example.ui.theme.AnimeDarkSurface
import com.example.ui.theme.AnimeGrey
import com.example.ui.theme.AnimeOnyx
import com.example.ui.theme.AnimeSurfaceVariant
import com.example.ui.theme.AnimeWhite
import com.example.ui.theme.AuraCyan
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.SuperSaiyanGold

@Composable
fun WorkoutCompleteScreen(
    uiState: WorkoutTimerUiState,
    onReturnHome: () -> Unit,
    onReplayQuoteAudio: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val routine = uiState.activeRoutine
    val quote = uiState.currentQuote
    val xpBreakdown = uiState.lastXpBreakdown

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AnimeOnyx)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(16.dp))

                // Victory Hero Cutscene Art
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .border(2.dp, SuperSaiyanGold, RoundedCornerShape(24.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_limit_break),
                        contentDescription = "Victory Anime Limit Break",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    0.0f to Color.Transparent,
                                    0.6f to AnimeOnyx.copy(alpha = 0.5f),
                                    1.0f to AnimeDarkSurface
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "VICTORY ACHIEVED!",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = SuperSaiyanGold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "YOU SURPASSED YOUR LIMITS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AuraCyan,
                            letterSpacing = 2.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // XP Earned Breakdown Card
                if (xpBreakdown != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.5.dp, FlameOrange.copy(alpha = 0.7f), RoundedCornerShape(20.dp)),
                        colors = CardDefaults.cardColors(containerColor = AnimeDarkSurface),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Bolt,
                                        contentDescription = null,
                                        tint = SuperSaiyanGold,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "POWER LEVEL XP GAINED",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black,
                                        color = AnimeWhite,
                                        letterSpacing = 0.5.sp
                                    )
                                }

                                Text(
                                    text = "+${xpBreakdown.totalXpEarned} XP",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = SuperSaiyanGold
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Breakdown rows
                            XpItemRow(
                                label = "Duration XP (${xpBreakdown.durationSeconds}s work)",
                                value = "+${xpBreakdown.durationXp} XP",
                                color = AuraCyan
                            )

                            if (xpBreakdown.intensityBonusXp > 0) {
                                Spacer(modifier = Modifier.height(4.dp))
                                XpItemRow(
                                    label = "Intensity Bonus (${xpBreakdown.intensityMultiplier}x)",
                                    value = "+${xpBreakdown.intensityBonusXp} XP",
                                    color = FlameOrange
                                )
                            }

                            if (xpBreakdown.clearBonusXp > 0) {
                                Spacer(modifier = Modifier.height(4.dp))
                                XpItemRow(
                                    label = "Boss Clear Bonus",
                                    value = "+${xpBreakdown.clearBonusXp} XP",
                                    color = SuperSaiyanGold
                                )
                            }

                            if (xpBreakdown.streakBonusXp > 0) {
                                Spacer(modifier = Modifier.height(4.dp))
                                XpItemRow(
                                    label = "Streak Bonus (+${xpBreakdown.streakMultiplierPercent}%)",
                                    value = "+${xpBreakdown.streakBonusXp} XP",
                                    color = SuperSaiyanGold
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(AnimeSurfaceVariant)
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "NEW POWER LEVEL:",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AnimeGrey
                                    )
                                    Text(
                                        text = "${uiState.userProfile.totalPowerLevel} XP (${uiState.userProfile.rankTitle})",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black,
                                        color = SuperSaiyanGold
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // Fallback Power Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, FlameOrange.copy(alpha = 0.5f), RoundedCornerShape(20.dp)),
                        colors = CardDefaults.cardColors(containerColor = AnimeDarkSurface),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "TOTAL POWER LEVEL",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = AnimeGrey
                            )
                            Text(
                                text = "${uiState.userProfile.totalPowerLevel} XP",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Black,
                                color = SuperSaiyanGold
                            )
                            Text(
                                text = "RANK: ${uiState.userProfile.rankTitle.uppercase()}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = FlameOrange
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Stats Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        icon = Icons.Default.CheckCircle,
                        title = "ROUNDS",
                        value = "${routine?.totalRounds ?: 8}",
                        tint = AuraCyan,
                        modifier = Modifier.weight(1f)
                    )

                    StatCard(
                        icon = Icons.Default.Timer,
                        title = "TIME",
                        value = "${(routine?.totalWorkoutSeconds ?: 300) / 60}m",
                        tint = SuperSaiyanGold,
                        modifier = Modifier.weight(1f)
                    )

                    StatCard(
                        icon = Icons.Default.LocalFireDepartment,
                        title = "BURN",
                        value = "${uiState.caloriesBurned} kcal",
                        tint = FlameOrange,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Anime Celebration Quote
                if (quote != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, AnimeCardBorder, RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = AnimeSurfaceVariant),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${quote.character} (${quote.anime})",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = SuperSaiyanGold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "\"${quote.quote}\"",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    fontStyle = FontStyle.Italic,
                                    color = AnimeWhite
                                )
                            }

                            IconButton(
                                onClick = { onReplayQuoteAudio(quote.quote) },
                                modifier = Modifier.testTag("victory_speak_quote_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Campaign,
                                    contentDescription = "Speak Quote",
                                    tint = AuraCyan
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Return Home Button
            Button(
                onClick = onReturnHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("claim_rewards_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = FlameOrange,
                    contentColor = AnimeWhite
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "CLAIM REWARDS & RETURN",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
private fun XpItemRow(
    label: String,
    value: String,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 12.sp, color = AnimeGrey)
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
private fun StatCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    tint: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.border(1.dp, AnimeCardBorder, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = AnimeDarkSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = AnimeWhite,
                textAlign = TextAlign.Center
            )
            Text(
                text = title,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = AnimeGrey
            )
        }
    }
}
