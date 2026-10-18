package com.example.timer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.BattleAudioEngine
import com.example.data.AnimeQuotesRepository
import com.example.data.WorkoutHistoryRepository
import com.example.data.WorkoutPresetRepository
import com.example.model.AnimeQuote
import com.example.model.UserPowerProfile
import com.example.model.WorkoutIntensity
import com.example.model.WorkoutPhase
import com.example.model.WorkoutRoutine
import com.example.model.WorkoutSessionRecord
import com.example.model.XpBreakdown
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WorkoutTimerUiState(
    val activeRoutine: WorkoutRoutine? = null,
    val phase: WorkoutPhase = WorkoutPhase.PREPARE,
    val secondsRemaining: Int = 10,
    val totalPhaseDuration: Int = 10,
    val currentRound: Int = 1,
    val totalRounds: Int = 8,
    val isRunning: Boolean = false,
    val isPaused: Boolean = false,
    val isMuted: Boolean = false,
    val isVoiceEnabled: Boolean = true,
    val currentQuote: AnimeQuote? = null,
    val currentPowerLevel: Int = 2500,
    val earnedPowerInSession: Int = 0,
    val caloriesBurned: Int = 0,
    val isCompleted: Boolean = false,
    val lastXpBreakdown: XpBreakdown? = null,
    val allRoutines: List<WorkoutRoutine> = emptyList(),
    val userProfile: UserPowerProfile = UserPowerProfile(),
    val sessionHistory: List<WorkoutSessionRecord> = emptyList()
)

class WorkoutTimerViewModel(application: Application) : AndroidViewModel(application) {

    private val presetRepo = WorkoutPresetRepository(application)
    private val historyRepo = WorkoutHistoryRepository(application)
    val audioEngine = BattleAudioEngine(application)

    private val _uiState = MutableStateFlow(WorkoutTimerUiState())
    val uiState: StateFlow<WorkoutTimerUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var totalSecondsWorked: Int = 0

    init {
        loadData()
    }

    fun loadData() {
        val presets = presetRepo.getAllPresets()
        val profile = historyRepo.getUserProfile()
        val history = historyRepo.getWorkoutHistory()
        _uiState.update {
            it.copy(
                allRoutines = presets,
                userProfile = profile,
                currentPowerLevel = profile.totalPowerLevel,
                sessionHistory = history
            )
        }
    }

    fun startWorkout(routine: WorkoutRoutine) {
        timerJob?.cancel()
        totalSecondsWorked = 0

        val initialQuote = AnimeQuotesRepository.getRandomQuoteForPhase(WorkoutPhase.PREPARE)

        _uiState.update {
            it.copy(
                activeRoutine = routine,
                phase = WorkoutPhase.PREPARE,
                secondsRemaining = routine.prepSeconds,
                totalPhaseDuration = routine.prepSeconds,
                currentRound = 1,
                totalRounds = routine.totalRounds,
                isRunning = true,
                isPaused = false,
                currentQuote = initialQuote,
                earnedPowerInSession = 0,
                caloriesBurned = 0,
                isCompleted = false,
                lastXpBreakdown = null
            )
        }

        audioEngine.speak("Get ready! Prepare your mind and body.", priority = true)
        startTimerLoop()
    }

    private fun startTimerLoop() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)

                val state = _uiState.value
                if (!state.isRunning || state.isPaused) {
                    continue
                }

                val newSeconds = state.secondsRemaining - 1

                if (state.phase == WorkoutPhase.WORK) {
                    totalSecondsWorked++
                }

                // Audio countdown tick for final 3 seconds
                if (newSeconds in 1..3) {
                    audioEngine.playCountdownBeep(newSeconds)
                }

                if (newSeconds <= 0) {
                    audioEngine.playCountdownBeep(0)
                    advancePhase()
                } else {
                    // Update seconds and live power calculation based on duration & intensity
                    val routine = state.activeRoutine
                    val intensityMultiplier = routine?.intensity?.multiplier ?: 1.0f
                    val powerMultiplier = if (state.phase == WorkoutPhase.WORK) {
                        (4 * intensityMultiplier).toInt().coerceAtLeast(4)
                    } else 1

                    val currentCals = ((totalSecondsWorked / 60f) * 11).toInt()
                    _uiState.update {
                        it.copy(
                            secondsRemaining = newSeconds,
                            earnedPowerInSession = it.earnedPowerInSession + powerMultiplier,
                            currentPowerLevel = it.userProfile.totalPowerLevel + it.earnedPowerInSession + powerMultiplier,
                            caloriesBurned = currentCals
                        )
                    }
                }
            }
        }
    }

    private fun advancePhase() {
        val state = _uiState.value
        val routine = state.activeRoutine ?: return

        when (state.phase) {
            WorkoutPhase.PREPARE -> {
                val quote = AnimeQuotesRepository.getRandomQuoteForPhase(WorkoutPhase.WORK)
                _uiState.update {
                    it.copy(
                        phase = WorkoutPhase.WORK,
                        secondsRemaining = routine.workSeconds,
                        totalPhaseDuration = routine.workSeconds,
                        currentRound = 1,
                        currentQuote = quote
                    )
                }
                audioEngine.speak("Round 1. Fight! ${quote.character} says: ${quote.quote}", priority = true)
            }

            WorkoutPhase.WORK -> {
                if (state.currentRound < state.totalRounds) {
                    val quote = AnimeQuotesRepository.getRandomQuoteForPhase(WorkoutPhase.REST)
                    _uiState.update {
                        it.copy(
                            phase = WorkoutPhase.REST,
                            secondsRemaining = routine.restSeconds,
                            totalPhaseDuration = routine.restSeconds,
                            currentQuote = quote
                        )
                    }
                    audioEngine.speak("Rest and power up. ${quote.quote}", priority = true)
                } else {
                    completeWorkout()
                }
            }

            WorkoutPhase.REST -> {
                val nextRound = state.currentRound + 1
                val quote = AnimeQuotesRepository.getRandomQuoteForPhase(WorkoutPhase.WORK)
                _uiState.update {
                    it.copy(
                        phase = WorkoutPhase.WORK,
                        secondsRemaining = routine.workSeconds,
                        totalPhaseDuration = routine.workSeconds,
                        currentRound = nextRound,
                        currentQuote = quote
                    )
                }
                audioEngine.speak("Round $nextRound. Unleash your power!", priority = true)
            }

            WorkoutPhase.COOLDOWN, WorkoutPhase.COMPLETED -> {
                // Already completed
            }
        }
    }

    private fun completeWorkout() {
        timerJob?.cancel()
        val state = _uiState.value
        val routine = state.activeRoutine ?: return

        val winQuote = AnimeQuotesRepository.getRandomQuoteForPhase(WorkoutPhase.COMPLETED)
        audioEngine.playVictoryFanfare()
        audioEngine.speak("Victory! Workout complete! You broke your limits!", priority = true)

        val totalSessionSecs = routine.prepSeconds + (routine.workSeconds + routine.restSeconds) * routine.totalRounds

        // Duration & Intensity based XP calculation
        val xpBreakdown = historyRepo.calculateXp(
            workDurationSeconds = totalSecondsWorked.coerceAtLeast(1),
            intensity = routine.intensity,
            completedRounds = state.currentRound,
            totalRounds = state.totalRounds,
            streakDays = state.userProfile.currentStreakDays
        )

        val sessionRecord = WorkoutSessionRecord(
            id = "sess_${System.currentTimeMillis()}",
            routineTitle = routine.title,
            timestamp = System.currentTimeMillis(),
            totalSeconds = totalSessionSecs,
            completedRounds = state.currentRound,
            totalRounds = routine.totalRounds,
            intensityName = routine.intensity.name,
            durationXp = xpBreakdown.durationXp,
            intensityBonusXp = xpBreakdown.intensityBonusXp,
            clearBonusXp = xpBreakdown.clearBonusXp,
            streakBonusXp = xpBreakdown.streakBonusXp,
            powerGained = xpBreakdown.totalXpEarned
        )

        val updatedProfile = historyRepo.recordWorkout(sessionRecord)
        val updatedHistory = historyRepo.getWorkoutHistory()

        _uiState.update {
            it.copy(
                phase = WorkoutPhase.COMPLETED,
                isRunning = false,
                isCompleted = true,
                currentQuote = winQuote,
                lastXpBreakdown = xpBreakdown,
                userProfile = updatedProfile,
                currentPowerLevel = updatedProfile.totalPowerLevel,
                sessionHistory = updatedHistory
            )
        }
    }

    fun pause() {
        _uiState.update { it.copy(isPaused = true) }
        audioEngine.speak("Workout paused.")
    }

    fun resume() {
        _uiState.update { it.copy(isPaused = false) }
        audioEngine.speak("Resuming battle!")
    }

    fun stop() {
        timerJob?.cancel()
        _uiState.update {
            it.copy(
                isRunning = false,
                isPaused = false,
                activeRoutine = null,
                phase = WorkoutPhase.PREPARE
            )
        }
    }

    fun skipPhase() {
        advancePhase()
    }

    fun toggleMute() {
        val newMute = !_uiState.value.isMuted
        audioEngine.setMuted(newMute)
        _uiState.update { it.copy(isMuted = newMute) }
    }

    fun toggleVoice() {
        val newVoice = !_uiState.value.isVoiceEnabled
        audioEngine.setVoiceEnabled(newVoice)
        _uiState.update { it.copy(isVoiceEnabled = newVoice) }
        if (newVoice) {
            audioEngine.speak("Anime battle voice enabled!")
        }
    }

    fun speakQuote(quote: String) {
        audioEngine.speak(quote, priority = true)
    }

    fun saveCustomRoutine(
        title: String,
        workSecs: Int,
        restSecs: Int,
        rounds: Int,
        animeTag: String,
        intensity: WorkoutIntensity = WorkoutIntensity.STANDARD
    ) {
        val newRoutine = WorkoutRoutine(
            id = "custom_${System.currentTimeMillis()}",
            title = title.ifBlank { "Custom Warrior Workout" },
            animeSeries = animeTag.ifBlank { "Custom Training" },
            description = "Custom anime intervals: ${workSecs}s Work / ${restSecs}s Rest • ${intensity.label}",
            prepSeconds = 10,
            workSeconds = workSecs,
            restSeconds = restSecs,
            totalRounds = rounds,
            bossName = "Final Limit",
            difficulty = intensity.label,
            intensity = intensity,
            estimatedCaloriesPerRound = (workSecs * 0.4).toInt(),
            isCustom = true
        )
        presetRepo.saveCustomPreset(newRoutine)
        loadData()
    }

    fun deleteCustomRoutine(id: String) {
        presetRepo.deleteCustomPreset(id)
        loadData()
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        audioEngine.release()
    }
}
