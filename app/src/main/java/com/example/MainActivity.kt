package com.example

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.WorkoutPhase
import com.example.timer.WorkoutTimerViewModel
import com.example.ui.screens.CustomRoutineDialog
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PowerRankScreen
import com.example.ui.screens.QuoteVaultScreen
import com.example.ui.screens.WorkoutCompleteScreen
import com.example.ui.screens.WorkoutTimerScreen
import com.example.ui.theme.AnimeDarkSurface
import com.example.ui.theme.AnimeGrey
import com.example.ui.theme.AnimeOnyx
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SuperSaiyanGold

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Keep screen on during workout so timer stays visible
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                AnimeWorkoutApp()
            }
        }
    }
}

@Composable
fun AnimeWorkoutApp(
    viewModel: WorkoutTimerViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    var showCreateDialog by remember { mutableStateOf(false) }

    if (showCreateDialog) {
        CustomRoutineDialog(
            onDismiss = { showCreateDialog = false },
            onSave = { title, work, rest, rounds, animeTag, intensity ->
                showCreateDialog = false
                viewModel.saveCustomRoutine(title, work, rest, rounds, animeTag, intensity)
            }
        )
    }

    // Full screen flows take priority over tabs
    when {
        uiState.isCompleted || uiState.phase == WorkoutPhase.COMPLETED -> {
            WorkoutCompleteScreen(
                uiState = uiState,
                onReturnHome = { viewModel.stop() },
                onReplayQuoteAudio = { quote -> viewModel.speakQuote(quote) }
            )
        }

        uiState.isRunning -> {
            WorkoutTimerScreen(
                uiState = uiState,
                onPause = { viewModel.pause() },
                onResume = { viewModel.resume() },
                onSkipPhase = { viewModel.skipPhase() },
                onStopWorkout = { viewModel.stop() },
                onToggleMute = { viewModel.toggleMute() },
                onToggleVoice = { viewModel.toggleVoice() },
                onReplayQuoteAudio = { quote -> viewModel.speakQuote(quote) }
            )
        }

        else -> {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AnimeOnyx),
                containerColor = AnimeOnyx,
                bottomBar = {
                    NavigationBar(
                        containerColor = AnimeDarkSurface,
                        contentColor = AnimeGrey,
                        tonalElevation = 8.dp,
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .testTag("bottom_nav_bar")
                    ) {
                        NavigationBarItem(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.FitnessCenter,
                                    contentDescription = "Workouts"
                                )
                            },
                            label = {
                                Text(
                                    "Battles",
                                    fontSize = 11.sp,
                                    fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = FlameOrange,
                                selectedTextColor = FlameOrange,
                                indicatorColor = FlameOrange.copy(alpha = 0.2f),
                                unselectedIconColor = AnimeGrey,
                                unselectedTextColor = AnimeGrey
                            ),
                            modifier = Modifier.testTag("nav_workouts")
                        )

                        NavigationBarItem(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.FormatQuote,
                                    contentDescription = "Quotes"
                                )
                            },
                            label = {
                                Text(
                                    "Vault",
                                    fontSize = 11.sp,
                                    fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SuperSaiyanGold,
                                selectedTextColor = SuperSaiyanGold,
                                indicatorColor = SuperSaiyanGold.copy(alpha = 0.2f),
                                unselectedIconColor = AnimeGrey,
                                unselectedTextColor = AnimeGrey
                            ),
                            modifier = Modifier.testTag("nav_quotes")
                        )

                        NavigationBarItem(
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = "Power Rank"
                                )
                            },
                            label = {
                                Text(
                                    "Rank",
                                    fontSize = 11.sp,
                                    fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SuperSaiyanGold,
                                selectedTextColor = SuperSaiyanGold,
                                indicatorColor = SuperSaiyanGold.copy(alpha = 0.2f),
                                unselectedIconColor = AnimeGrey,
                                unselectedTextColor = AnimeGrey
                            ),
                            modifier = Modifier.testTag("nav_rank")
                        )
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    AnimatedContent(
                        targetState = selectedTab,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "tabAnim"
                    ) { tab ->
                        when (tab) {
                            0 -> HomeScreen(
                                uiState = uiState,
                                onStartRoutine = { routine -> viewModel.startWorkout(routine) },
                                onOpenCreateDialog = { showCreateDialog = true },
                                onDeleteCustomRoutine = { id -> viewModel.deleteCustomRoutine(id) },
                                onPlayQuoteAudio = { quote -> viewModel.speakQuote(quote) }
                            )

                            1 -> QuoteVaultScreen(
                                onPlayQuoteAudio = { quote -> viewModel.speakQuote(quote) }
                            )

                            2 -> PowerRankScreen(
                                uiState = uiState
                            )
                        }
                    }
                }
            }
        }
    }
}
