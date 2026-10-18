package com.example.model

enum class WorkoutPhase(val label: String, val subtitle: String) {
    PREPARE("PREPARE", "Focus your spirit & breathing..."),
    WORK("FIGHT / WORK", "UNLEASH FULL POWER! SURPASS YOUR LIMITS!"),
    REST("REST & RECOVER", "Regather your Ki. Steady your breath."),
    COOLDOWN("VICTORY", "Battle Complete! Well fought, warrior!"),
    COMPLETED("FINISHED", "You have broken through your ceiling!")
}

enum class WorkoutIntensity(
    val label: String,
    val multiplier: Float,
    val xpPerWorkSecond: Int,
    val clearBonusXp: Int,
    val colorHex: Long
) {
    CASUAL("Casual", 1.0f, 3, 200, 0xFF00E5FF),
    STANDARD("Standard", 1.5f, 5, 450, 0xFFFFD600),
    INTENSE("Intense", 2.0f, 8, 750, 0xFFFF5500),
    LIMIT_BREAKER("Limit Breaker", 3.0f, 12, 1200, 0xFFFF1744);

    companion object {
        fun fromString(name: String?): WorkoutIntensity {
            return entries.firstOrNull {
                it.name.equals(name, ignoreCase = true) || it.label.equals(name, ignoreCase = true)
            } ?: STANDARD
        }
    }
}

data class WorkoutRoutine(
    val id: String,
    val title: String,
    val animeSeries: String,
    val description: String,
    val prepSeconds: Int = 10,
    val workSeconds: Int = 30,
    val restSeconds: Int = 15,
    val totalRounds: Int = 8,
    val bossName: String = "Limit Breaker",
    val difficulty: String = "Intermediate",
    val intensity: WorkoutIntensity = WorkoutIntensity.STANDARD,
    val estimatedCaloriesPerRound: Int = 12,
    val isCustom: Boolean = false
) {
    val totalWorkoutSeconds: Int
        get() = prepSeconds + (workSeconds + restSeconds) * totalRounds - restSeconds
}

data class AnimeQuote(
    val id: String,
    val quote: String,
    val character: String,
    val anime: String,
    val japaneseRomanji: String? = null,
    val phase: WorkoutPhase = WorkoutPhase.WORK,
    val powerLevelHype: Int = 9000
)

data class XpBreakdown(
    val durationSeconds: Int,
    val durationXp: Int,
    val intensityMultiplier: Float,
    val intensityBonusXp: Int,
    val completedRounds: Int,
    val roundsBonusXp: Int,
    val clearBonusXp: Int,
    val streakMultiplierPercent: Int,
    val streakBonusXp: Int,
    val totalXpEarned: Int
)

data class WorkoutSessionRecord(
    val id: String,
    val routineTitle: String,
    val timestamp: Long,
    val totalSeconds: Int,
    val completedRounds: Int,
    val totalRounds: Int,
    val intensityName: String = WorkoutIntensity.STANDARD.name,
    val durationXp: Int = 0,
    val intensityBonusXp: Int = 0,
    val clearBonusXp: Int = 0,
    val streakBonusXp: Int = 0,
    val powerGained: Int = 0
)

data class UserPowerProfile(
    val totalPowerLevel: Int = 1250,
    val workoutsCompleted: Int = 0,
    val totalSecondsTrained: Int = 0,
    val currentStreakDays: Int = 1,
    val level: Int = 1,
    val rankTitle: String = "E-Rank Hunter",
    val currentLevelMinXp: Int = 0,
    val nextLevelTargetXp: Int = 1500,
    val progressFraction: Float = 0.5f
)
