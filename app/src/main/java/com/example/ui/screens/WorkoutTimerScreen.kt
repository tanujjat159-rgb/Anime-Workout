package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.WorkoutPhase
import com.example.timer.WorkoutTimerUiState
import com.example.ui.components.AnimeFightCard
import com.example.ui.components.AnimeKiAuraRing
import com.example.ui.components.AnimeSpeedlinesCanvas
import com.example.ui.components.ScouterHudOverlay
import com.example.ui.theme.AnimeDarkSurface
import com.example.ui.theme.AnimeGrey
import com.example.ui.theme.AnimeOnyx
import com.example.ui.theme.AnimeWhite
import com.example.ui.theme.CrimsonRage
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.SuperSaiyanGold

@Composable
fun WorkoutTimerScreen(
    uiState: WorkoutTimerUiState,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onSkipPhase: () -> Unit,
    onStopWorkout: () -> Unit,
    onToggleMute: () -> Unit,
    onToggleVoice: () -> Unit,
    onReplayQuoteAudio: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showExitDialog by remember { mutableStateOf(false) }

    // Intercept hardware and gesture back
    BackHandler {
        showExitDialog = true
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = {
                Text(
                    text = "ABORT WORKOUT?",
                    fontWeight = FontWeight.Black,
                    color = CrimsonRage
                )
            },
            text = {
                Text(
                    text = "A true warrior never yields! Are you sure you want to stop this training session?",
                    color = AnimeWhite
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showExitDialog = false
                        onStopWorkout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonRage),
                    modifier = Modifier.testTag("confirm_abort_button")
                ) {
                    Text("RETREAT")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showExitDialog = false },
                    modifier = Modifier.testTag("dismiss_abort_button")
                ) {
                    Text("KEEP FIGHTING", color = SuperSaiyanGold)
                }
            },
            containerColor = AnimeDarkSurface
        )
    }

    val routine = uiState.activeRoutine

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AnimeOnyx)
    ) {
        // Dynamic Anime Speedlines Layer during WORK interval
        AnimeSpeedlinesCanvas(
            isActive = uiState.phase == WorkoutPhase.WORK && !uiState.isPaused,
            accentColor = FlameOrange
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { showExitDialog = true },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(AnimeDarkSurface)
                        .testTag("back_from_timer_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = AnimeWhite
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = (routine?.title ?: "ANIME INTERVALS").uppercase(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = AnimeWhite,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "VS ${routine?.bossName ?: "LIMIT BREAKER"}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = FlameOrange
                    )
                }

                IconButton(
                    onClick = { showExitDialog = true },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(AnimeDarkSurface)
                        .testTag("close_timer_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Abort Workout",
                        tint = CrimsonRage
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Scouter HUD (Power level, calories, audio toggles)
            ScouterHudOverlay(
                currentPowerLevel = uiState.currentPowerLevel,
                burnedCalories = uiState.caloriesBurned,
                isMuted = uiState.isMuted,
                isVoiceEnabled = uiState.isVoiceEnabled,
                onToggleMute = onToggleMute,
                onToggleVoice = onToggleVoice,
                modifier = Modifier.testTag("scouter_hud")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Anime Fight Scene Card with dynamic dialogue quote
            AnimeFightCard(
                currentPhase = uiState.phase,
                currentQuote = uiState.currentQuote,
                onReplayQuoteAudio = onReplayQuoteAudio,
                modifier = Modifier.testTag("anime_fight_card")
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Center Ki Aura Ring Countdown
            AnimeKiAuraRing(
                secondsRemaining = uiState.secondsRemaining,
                totalPhaseDuration = uiState.totalPhaseDuration,
                phase = uiState.phase,
                currentRound = uiState.currentRound,
                totalRounds = uiState.totalRounds,
                modifier = Modifier.testTag("ki_aura_ring")
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Action Control Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Main Pause/Resume Button
                Button(
                    onClick = {
                        if (uiState.isPaused) onResume() else onPause()
                    },
                    modifier = Modifier
                        .weight(1.3f)
                        .height(54.dp)
                        .testTag("pause_resume_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (uiState.isPaused) SuperSaiyanGold else FlameOrange,
                        contentColor = if (uiState.isPaused) AnimeOnyx else AnimeWhite
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        imageVector = if (uiState.isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (uiState.isPaused) "RESUME" else "PAUSE",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }

                // Skip Phase Button
                Button(
                    onClick = onSkipPhase,
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp)
                        .testTag("skip_phase_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AnimeDarkSurface,
                        contentColor = AnimeWhite
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FastForward,
                        contentDescription = null,
                        tint = SuperSaiyanGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "SKIP",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
