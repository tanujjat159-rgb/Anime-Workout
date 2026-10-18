package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.example.data.AnimeQuotesRepository
import com.example.model.WorkoutPhase
import com.example.model.WorkoutRoutine
import com.example.timer.WorkoutTimerUiState
import com.example.ui.theme.AnimeCardBorder
import com.example.ui.theme.AnimeDarkSurface
import com.example.ui.theme.AnimeGrey
import com.example.ui.theme.AnimeOnyx
import com.example.ui.theme.AnimeSurfaceVariant
import com.example.ui.theme.AnimeWhite
import com.example.ui.theme.AuraCyan
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.FlameOrangeGlow
import com.example.ui.theme.SuperSaiyanGold

@Composable
fun HomeScreen(
    uiState: WorkoutTimerUiState,
    onStartRoutine: (WorkoutRoutine) -> Unit,
    onOpenCreateDialog: () -> Unit,
    onDeleteCustomRoutine: (String) -> Unit,
    onPlayQuoteAudio: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val dailyQuote = remember {
        AnimeQuotesRepository.getRandomQuoteForPhase(WorkoutPhase.WORK)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AnimeOnyx),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.5.dp, FlameOrange.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_fight_battle),
                    contentDescription = "Anime Battle Hero",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Dramatic Dark Gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                0.0f to Color.Transparent,
                                0.5f to AnimeOnyx.copy(alpha = 0.7f),
                                1.0f to AnimeDarkSurface
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Top Rank Pill
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(AnimeDarkSurface.copy(alpha = 0.9f))
                                .border(1.dp, SuperSaiyanGold, RoundedCornerShape(12.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = SuperSaiyanGold,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = uiState.userProfile.rankTitle.uppercase(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = SuperSaiyanGold,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }

                        // Power Level Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(FlameOrange)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "PL: ${uiState.userProfile.totalPowerLevel}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = AnimeWhite
                            )
                        }
                    }

                    // Title & Description
                    Column {
                        Text(
                            text = "ANIME WORKOUT TIMER",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = AnimeWhite,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Surpass your limits with fight scenes & anime roars!",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = AuraCyan
                        )
                    }
                }
            }
        }

        // Daily Motivation Quote Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, AuraCyan.copy(alpha = 0.4f), RoundedCornerShape(18.dp)),
                colors = CardDefaults.cardColors(containerColor = AnimeDarkSurface),
                shape = RoundedCornerShape(18.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(AuraCyan.copy(alpha = 0.2f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "QUOTE OF THE DAY",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    color = AuraCyan
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "• ${dailyQuote.character}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SuperSaiyanGold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "\"${dailyQuote.quote}\"",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontStyle = FontStyle.Italic,
                            color = AnimeWhite,
                            maxLines = 2
                        )
                    }

                    IconButton(
                        onClick = { onPlayQuoteAudio(dailyQuote.quote) },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(AuraCyan.copy(alpha = 0.15f))
                            .testTag("home_quote_audio_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Campaign,
                            contentDescription = "Speak Quote",
                            tint = AuraCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Routines Section Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = null,
                        tint = FlameOrange,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "BATTLE INTERVAL ROUTINES",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = AnimeWhite,
                        letterSpacing = 0.5.sp
                    )
                }

                Button(
                    onClick = onOpenCreateDialog,
                    colors = ButtonDefaults.buttonColors(containerColor = AnimeSurfaceVariant),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("create_routine_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = SuperSaiyanGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "NEW ROUTINE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SuperSaiyanGold
                    )
                }
            }
        }

        // List of Routines
        items(uiState.allRoutines, key = { it.id }) { routine ->
            RoutineCard(
                routine = routine,
                onStart = { onStartRoutine(routine) },
                onDelete = if (routine.isCustom) {
                    { onDeleteCustomRoutine(routine.id) }
                } else null
            )
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun RoutineCard(
    routine: WorkoutRoutine,
    onStart: () -> Unit,
    onDelete: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, AnimeCardBorder, RoundedCornerShape(18.dp))
            .clickable { onStart() }
            .testTag("routine_card_${routine.id}"),
        colors = CardDefaults.cardColors(containerColor = AnimeDarkSurface),
        shape = RoundedCornerShape(18.dp)
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
                // Anime Tag Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(FlameOrange.copy(alpha = 0.18f))
                        .border(1.dp, FlameOrange.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = routine.animeSeries.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = FlameOrangeGlow
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    val intensityColor = Color(routine.intensity.colorHex)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(intensityColor.copy(alpha = 0.2f))
                            .border(1.dp, intensityColor.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${routine.intensity.label.uppercase()} (${routine.intensity.multiplier}x XP)",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = intensityColor
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = routine.difficulty,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SuperSaiyanGold
                    )

                    if (onDelete != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = onDelete,
                            modifier = Modifier
                                .size(28.dp)
                                .testTag("delete_routine_${routine.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Routine",
                                tint = AnimeGrey,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = routine.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AnimeWhite
            )

            Text(
                text = routine.description,
                fontSize = 12.sp,
                color = AnimeGrey,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Specs Row & Start Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = FlameOrange,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${routine.workSeconds}s / ${routine.restSeconds}s",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AnimeWhite
                        )
                    }

                    Text(
                        text = "• ${routine.totalRounds} Rounds",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = AuraCyan
                    )
                }

                Button(
                    onClick = onStart,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FlameOrange,
                        contentColor = AnimeWhite
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("start_routine_button_${routine.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "BATTLE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}
