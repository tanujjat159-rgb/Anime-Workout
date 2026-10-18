package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.UserPowerProfile
import com.example.model.WorkoutIntensity
import com.example.model.WorkoutSessionRecord
import com.example.model.XpBreakdown
import org.json.JSONArray
import org.json.JSONObject

data class RankTier(
    val level: Int,
    val title: String,
    val minXp: Int,
    val maxXp: Int,
    val animeArchetype: String
)

class WorkoutHistoryRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("anime_workout_history", Context.MODE_PRIVATE)

    companion object {
        val RANK_TIERS = listOf(
            RankTier(1, "E-Rank Hunter", 0, 1500, "Solo Leveling"),
            RankTier(2, "Genin Shinobi", 1500, 3500, "Naruto"),
            RankTier(3, "Chunin Fighter", 3500, 7000, "Dragon Ball"),
            RankTier(4, "Demon Slayer Corps", 7000, 12000, "Demon Slayer"),
            RankTier(5, "Class 1-A Hero", 12000, 19000, "My Hero Academia"),
            RankTier(6, "Flame Hashira", 19000, 28000, "Demon Slayer"),
            RankTier(7, "Super Saiyan 1", 28000, 40000, "Dragon Ball Z"),
            RankTier(8, "Special Grade Sorcerer", 40000, 55000, "Jujutsu Kaisen"),
            RankTier(9, "Shadow Monarch", 55000, 75000, "Solo Leveling"),
            RankTier(10, "Ultra Instinct God", 75000, 120000, "Dragon Ball Super")
        )
    }

    fun calculateXp(
        workDurationSeconds: Int,
        intensity: WorkoutIntensity,
        completedRounds: Int,
        totalRounds: Int,
        streakDays: Int
    ): XpBreakdown {
        // Duration Base: 4 XP per work second
        val durationXp = workDurationSeconds * 4

        // Intensity Bonus: based on multiplier above 1.0x
        val intensityBonusXp = (durationXp * (intensity.multiplier - 1.0f)).toInt()

        // Rounds Bonus: 40 XP per round completed
        val roundsBonusXp = completedRounds * 40

        // Clear Bonus: full bonus if all rounds finished, partial if aborted early
        val clearBonusXp = if (completedRounds >= totalRounds) {
            intensity.clearBonusXp
        } else {
            ((intensity.clearBonusXp * (completedRounds.toFloat() / totalRounds.coerceAtLeast(1))) / 2).toInt()
        }

        // Streak Multiplier: +5% per day up to 50%
        val streakPct = (streakDays * 5).coerceIn(0, 50)
        val subtotal = durationXp + intensityBonusXp + roundsBonusXp + clearBonusXp
        val streakBonusXp = (subtotal * (streakPct / 100f)).toInt()
        val totalXpEarned = subtotal + streakBonusXp

        return XpBreakdown(
            durationSeconds = workDurationSeconds,
            durationXp = durationXp,
            intensityMultiplier = intensity.multiplier,
            intensityBonusXp = intensityBonusXp,
            completedRounds = completedRounds,
            roundsBonusXp = roundsBonusXp,
            clearBonusXp = clearBonusXp,
            streakMultiplierPercent = streakPct,
            streakBonusXp = streakBonusXp,
            totalXpEarned = totalXpEarned
        )
    }

    fun getUserProfile(): UserPowerProfile {
        val totalPower = prefs.getInt("total_power_level", 2500)
        val workoutsCount = prefs.getInt("workouts_completed", 0)
        val totalSecs = prefs.getInt("total_seconds_trained", 0)
        val streak = prefs.getInt("streak_days", 1)

        val tier = getTierForPower(totalPower)
        val progress = if (tier.maxXp > tier.minXp) {
            ((totalPower - tier.minXp).toFloat() / (tier.maxXp - tier.minXp).toFloat()).coerceIn(0f, 1f)
        } else 1f

        return UserPowerProfile(
            totalPowerLevel = totalPower,
            workoutsCompleted = workoutsCount,
            totalSecondsTrained = totalSecs,
            currentStreakDays = streak,
            level = tier.level,
            rankTitle = tier.title,
            currentLevelMinXp = tier.minXp,
            nextLevelTargetXp = tier.maxXp,
            progressFraction = progress
        )
    }

    fun recordWorkout(session: WorkoutSessionRecord): UserPowerProfile {
        val history = getWorkoutHistory().toMutableList()
        history.add(0, session)
        saveHistory(history)

        val currentPower = prefs.getInt("total_power_level", 2500)
        val workoutsCount = prefs.getInt("workouts_completed", 0)
        val totalSecs = prefs.getInt("total_seconds_trained", 0)

        val newPower = currentPower + session.powerGained
        val newCount = workoutsCount + 1
        val newSecs = totalSecs + session.totalSeconds

        prefs.edit()
            .putInt("total_power_level", newPower)
            .putInt("workouts_completed", newCount)
            .putInt("total_seconds_trained", newSecs)
            .apply()

        return getUserProfile()
    }

    fun getWorkoutHistory(): List<WorkoutSessionRecord> {
        val jsonString = prefs.getString("workout_history_json", null) ?: return emptyList()
        val list = mutableListOf<WorkoutSessionRecord>()
        try {
            val arr = JSONArray(jsonString)
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(
                    WorkoutSessionRecord(
                        id = obj.getString("id"),
                        routineTitle = obj.getString("routineTitle"),
                        timestamp = obj.getLong("timestamp"),
                        totalSeconds = obj.getInt("totalSeconds"),
                        completedRounds = obj.getInt("completedRounds"),
                        totalRounds = obj.getInt("totalRounds"),
                        intensityName = obj.optString("intensityName", WorkoutIntensity.STANDARD.name),
                        durationXp = obj.optInt("durationXp", 0),
                        intensityBonusXp = obj.optInt("intensityBonusXp", 0),
                        clearBonusXp = obj.optInt("clearBonusXp", 0),
                        streakBonusXp = obj.optInt("streakBonusXp", 0),
                        powerGained = obj.getInt("powerGained")
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    private fun saveHistory(list: List<WorkoutSessionRecord>) {
        val arr = JSONArray()
        for (item in list.take(50)) { // keep last 50
            val obj = JSONObject()
            obj.put("id", item.id)
            obj.put("routineTitle", item.routineTitle)
            obj.put("timestamp", item.timestamp)
            obj.put("totalSeconds", item.totalSeconds)
            obj.put("completedRounds", item.completedRounds)
            obj.put("totalRounds", item.totalRounds)
            obj.put("intensityName", item.intensityName)
            obj.put("durationXp", item.durationXp)
            obj.put("intensityBonusXp", item.intensityBonusXp)
            obj.put("clearBonusXp", item.clearBonusXp)
            obj.put("streakBonusXp", item.streakBonusXp)
            obj.put("powerGained", item.powerGained)
            arr.put(obj)
        }
        prefs.edit().putString("workout_history_json", arr.toString()).apply()
    }

    fun getTierForPower(power: Int): RankTier {
        for (tier in RANK_TIERS) {
            if (power in tier.minXp until tier.maxXp) {
                return tier
            }
        }
        return RANK_TIERS.last()
    }
}
