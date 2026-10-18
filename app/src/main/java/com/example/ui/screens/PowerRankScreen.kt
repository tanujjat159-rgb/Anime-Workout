package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.WorkoutHistoryRepository
import com.example.model.WorkoutIntensity
import com.example.model.WorkoutSessionRecord
import com.example.timer.WorkoutTimerUiState
import com.example.ui.theme.AnimeCardBorder
import com.example.ui.theme.AnimeDarkSurface
import com.example.ui.theme.AnimeGrey
import com.example.ui.theme.AnimeOnyx
import com.example.ui.theme.AnimeSurfaceVariant
import com.example.ui.theme.AnimeWhite
import com.example.ui.theme.AuraCyan
import com.example.ui.theme.CrimsonRage
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.SuperSaiyanGold
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PowerRankScreen(
    uiState: WorkoutTimerUiState,
    modifier: Modifier = Modifier
) {
    val profile = uiState.userProfile
    val power = profile.totalPowerLevel

    val tierRange = profile.nextLevelTargetXp - profile.currentLevelMinXp
    val progressInLevel = if (tierRange > 0) {
        ((power - profile.currentLevelMinXp).toFloat() / tierRange.toFloat()).coerceIn(0f, 1f)
    } else 1f
    val percentInt = (progressInLevel * 100).toInt()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AnimeOnyx),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Main Profile & Power Level XP Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, SuperSaiyanGold.copy(alpha = 0.6f), RoundedCornerShape(24.dp))
                    .testTag("profile_power_card"),
                colors = CardDefaults.cardColors(containerColor = AnimeDarkSurface),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(SuperSaiyanGold.copy(alpha = 0.15f))
                            .border(1.5.dp, SuperSaiyanGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = SuperSaiyanGold,
                            modifier = Modifier.size(34.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(FlameOrange)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "LEVEL ${profile.level}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = AnimeWhite
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = profile.rankTitle.uppercase(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = SuperSaiyanGold,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "$power XP",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = AnimeWhite
                    )

                    Text(
                        text = "TOTAL POWER LEVEL",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = AnimeGrey,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Progress to Next Rank
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "LEVEL PROGRESS: $percentInt%",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = AuraCyan
                            )
                            Text(
                                text = "$power / ${profile.nextLevelTargetXp} XP",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AnimeGrey
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        LinearProgressIndicator(
                            progress = { progressInLevel },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(5.dp)),
                            color = SuperSaiyanGold,
                            trackColor = AnimeSurfaceVariant,
                            strokeCap = StrokeCap.Round
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        val remainingXp = (profile.nextLevelTargetXp - power).coerceAtLeast(0)
                        Text(
                            text = "$remainingXp XP needed for next rank promotion",
                            fontSize = 10.sp,
                            color = AnimeGrey,
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
            }
        }

        // Stats Overview Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OverviewCard(
                    icon = Icons.Default.FitnessCenter,
                    label = "WORKOUTS",
                    value = "${profile.workoutsCompleted}",
                    tint = FlameOrange,
                    modifier = Modifier.weight(1f)
                )

                OverviewCard(
                    icon = Icons.Default.LocalFireDepartment,
                    label = "STREAK",
                    value = "${profile.currentStreakDays}d",
                    tint = SuperSaiyanGold,
                    modifier = Modifier.weight(1f)
                )

                OverviewCard(
                    icon = Icons.Default.Timer,
                    label = "TRAINED",
                    value = "${profile.totalSecondsTrained / 60}m",
                    tint = AuraCyan,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // XP System Guide Card (How Power Level is calculated)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, AnimeCardBorder, RoundedCornerShape(18.dp)),
                colors = CardDefaults.cardColors(containerColor = AnimeDarkSurface),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = AuraCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "POWER LEVEL XP CALCULATION",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = AnimeWhite,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    XpFormulaRow(
                        title = "Duration Base",
                        desc = "4 XP per second of active work interval (e.g. 5 min = 1,200 XP)",
                        color = AuraCyan
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    XpFormulaRow(
                        title = "Intensity Multipliers",
                        desc = "Casual (1.0x) • Standard (1.5x) • Intense (2.0x) • Limit Breaker (3.0x)",
                        color = FlameOrange
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    XpFormulaRow(
                        title = "Clear & Streak Bonus",
                        desc = "+200 to +1,200 XP boss clear + 5% bonus XP per day of active streak!",
                        color = SuperSaiyanGold
                    )
                }
            }
        }

        // Rank Tiers Ladder
        item {
            Text(
                text = "WARRIOR RANK LADDER",
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = AnimeWhite,
                letterSpacing = 0.5.sp
            )
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, AnimeCardBorder, RoundedCornerShape(18.dp)),
                colors = CardDefaults.cardColors(containerColor = AnimeDarkSurface),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    WorkoutHistoryRepository.RANK_TIERS.forEach { tier ->
                        val isCurrent = tier.level == profile.level
                        val isUnlocked = tier.level < profile.level

                        val bgColor = when {
                            isCurrent -> SuperSaiyanGold.copy(alpha = 0.15f)
                            isUnlocked -> AnimeSurfaceVariant
                            else -> AnimeDarkSurface
                        }

                        val borderColor = when {
                            isCurrent -> SuperSaiyanGold
                            isUnlocked -> AnimeCardBorder
                            else -> Color.Transparent
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(bgColor)
                                .border(1.dp, borderColor, RoundedCornerShape(10.dp))
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = when {
                                        isCurrent -> Icons.Default.Bolt
                                        isUnlocked -> Icons.Default.CheckCircle
                                        else -> Icons.Default.Lock
                                    },
                                    contentDescription = null,
                                    tint = when {
                                        isCurrent -> SuperSaiyanGold
                                        isUnlocked -> AuraCyan
                                        else -> AnimeGrey.copy(alpha = 0.4f)
                                    },
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "LVL ${tier.level} • ${tier.title}",
                                        fontSize = 12.sp,
                                        fontWeight = if (isCurrent) FontWeight.Black else FontWeight.SemiBold,
                                        color = if (isCurrent) SuperSaiyanGold else if (isUnlocked) AnimeWhite else AnimeGrey
                                    )
                                    Text(
                                        text = "${tier.animeArchetype} • ${tier.minXp} XP",
                                        fontSize = 10.sp,
                                        color = AnimeGrey
                                    )
                                }
                            }

                            if (isCurrent) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(SuperSaiyanGold)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "CURRENT",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        color = AnimeOnyx
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Training History Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = FlameOrange,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "BATTLE TRAINING LOG & XP BREAKDOWN",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = AnimeWhite,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // History items or empty state
        if (uiState.sessionHistory.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, AnimeCardBorder, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = AnimeDarkSurface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = AnimeGrey,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No battles logged yet!",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = AnimeWhite
                        )
                        Text(
                            text = "Complete your first interval workout to earn duration & intensity XP.",
                            fontSize = 12.sp,
                            color = AnimeGrey
                        )
                    }
                }
            }
        } else {
            items(uiState.sessionHistory, key = { it.id }) { session ->
                SessionHistoryItemCard(session)
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun XpFormulaRow(
    title: String,
    desc: String,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
            Text(text = desc, fontSize = 11.sp, color = AnimeGrey, lineHeight = 15.sp)
        }
    }
}

@Composable
private fun OverviewCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
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
                color = AnimeWhite
            )
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = AnimeGrey
            )
        }
    }
}

@Composable
private fun SessionHistoryItemCard(session: WorkoutSessionRecord) {
    val dateStr = SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault())
        .format(Date(session.timestamp))

    val intensity = WorkoutIntensity.fromString(session.intensityName)
    val intensityColor = Color(intensity.colorHex)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, AnimeCardBorder, RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = AnimeDarkSurface),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = session.routineTitle,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = AnimeWhite
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(intensityColor.copy(alpha = 0.2f))
                                .border(1.dp, intensityColor.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = intensity.label.uppercase(),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = intensityColor
                            )
                        }
                    }
                    Text(
                        text = "$dateStr • ${session.completedRounds}/${session.totalRounds} rounds",
                        fontSize = 11.sp,
                        color = AnimeGrey
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SuperSaiyanGold.copy(alpha = 0.15f))
                        .border(1.dp, SuperSaiyanGold.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "+${session.powerGained} XP",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = SuperSaiyanGold
                    )
                }
            }

            // Detailed XP Breakdown chips if available
            if (session.durationXp > 0 || session.intensityBonusXp > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    XpMiniChip(label = "Duration", xp = session.durationXp, color = AuraCyan)
                    if (session.intensityBonusXp > 0) {
                        XpMiniChip(label = "Intensity", xp = session.intensityBonusXp, color = FlameOrange)
                    }
                    if (session.clearBonusXp > 0) {
                        XpMiniChip(label = "Clear", xp = session.clearBonusXp, color = SuperSaiyanGold)
                    }
                }
            }
        }
    }
}

@Composable
private fun XpMiniChip(label: String, xp: Int, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(AnimeSurfaceVariant)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = "$label: +$xp",
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
    }
}
