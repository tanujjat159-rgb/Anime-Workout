package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.WorkoutIntensity
import com.example.model.WorkoutRoutine
import org.json.JSONArray
import org.json.JSONObject

class WorkoutPresetRepository(private val context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("anime_workout_presets", Context.MODE_PRIVATE)

    val defaultPresets: List<WorkoutRoutine> = listOf(
        WorkoutRoutine(
            id = "preset_opm",
            title = "One Punch Man HIIT",
            animeSeries = "One Punch Man",
            description = "High-octane bodyweight intervals: push-ups, squats & core burn.",
            prepSeconds = 10,
            workSeconds = 20,
            restSeconds = 10,
            totalRounds = 10,
            bossName = "Deep Sea King",
            difficulty = "Hero Class A",
            intensity = WorkoutIntensity.STANDARD,
            estimatedCaloriesPerRound = 14
        ),
        WorkoutRoutine(
            id = "preset_saiyan",
            title = "Hyperbolic Time Chamber",
            animeSeries = "Dragon Ball Super",
            description = "Heavy gravity resistance training. Surpass your Super Saiyan limits.",
            prepSeconds = 10,
            workSeconds = 40,
            restSeconds = 20,
            totalRounds = 8,
            bossName = "Lord Beerus",
            difficulty = "God of Destruction",
            intensity = WorkoutIntensity.LIMIT_BREAKER,
            estimatedCaloriesPerRound = 20
        ),
        WorkoutRoutine(
            id = "preset_sl",
            title = "Solo Leveling Daily Quest",
            animeSeries = "Solo Leveling",
            description = "Survive the system workout penalty and claim your stat point upgrades.",
            prepSeconds = 10,
            workSeconds = 45,
            restSeconds = 15,
            totalRounds = 6,
            bossName = "Blood-Red Commander Igris",
            difficulty = "S-Rank Dungeon",
            intensity = WorkoutIntensity.INTENSE,
            estimatedCaloriesPerRound = 22
        ),
        WorkoutRoutine(
            id = "preset_mha",
            title = "Plus Ultra Tabata Blast",
            animeSeries = "My Hero Academia",
            description = "Fast explosive Tabata sets. Clench your fists and break the ceiling!",
            prepSeconds = 10,
            workSeconds = 20,
            restSeconds = 10,
            totalRounds = 8,
            bossName = "Nomu",
            difficulty = "Pro Hero",
            intensity = WorkoutIntensity.INTENSE,
            estimatedCaloriesPerRound = 15
        ),
        WorkoutRoutine(
            id = "preset_ds",
            title = "Total Concentration Breathing",
            animeSeries = "Demon Slayer",
            description = "Focus your spiritual lungs, constant muscle tension, and steady stamina.",
            prepSeconds = 10,
            workSeconds = 30,
            restSeconds = 15,
            totalRounds = 6,
            bossName = "Upper Moon Akaza",
            difficulty = "Hashira Trial",
            intensity = WorkoutIntensity.STANDARD,
            estimatedCaloriesPerRound = 16
        ),
        WorkoutRoutine(
            id = "preset_jjk",
            title = "Black Flash Rush",
            animeSeries = "Jujutsu Kaisen",
            description = "Sparks of black cursed energy! Fast strikes and rhythmic power bursts.",
            prepSeconds = 10,
            workSeconds = 35,
            restSeconds = 20,
            totalRounds = 7,
            bossName = "Special Grade Mahito",
            difficulty = "Special Grade",
            intensity = WorkoutIntensity.INTENSE,
            estimatedCaloriesPerRound = 18
        )
    )

    fun getAllPresets(): List<WorkoutRoutine> {
        val customPresets = loadCustomPresets()
        return defaultPresets + customPresets
    }

    fun saveCustomPreset(routine: WorkoutRoutine) {
        val current = loadCustomPresets().toMutableList()
        current.removeAll { it.id == routine.id }
        current.add(0, routine.copy(isCustom = true))
        savePresetsList(current)
    }

    fun deleteCustomPreset(routineId: String) {
        val current = loadCustomPresets().toMutableList()
        current.removeAll { it.id == routineId }
        savePresetsList(current)
    }

    private fun loadCustomPresets(): List<WorkoutRoutine> {
        val jsonString = prefs.getString("custom_routines_json", null) ?: return emptyList()
        val list = mutableListOf<WorkoutRoutine>()
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    WorkoutRoutine(
                        id = obj.getString("id"),
                        title = obj.getString("title"),
                        animeSeries = obj.optString("animeSeries", "Custom Training"),
                        description = obj.optString("description", "Custom anime interval routine"),
                        prepSeconds = obj.optInt("prepSeconds", 10),
                        workSeconds = obj.optInt("workSeconds", 30),
                        restSeconds = obj.optInt("restSeconds", 15),
                        totalRounds = obj.optInt("totalRounds", 8),
                        bossName = obj.optString("bossName", "Rival Warrior"),
                        difficulty = obj.optString("difficulty", "Warrior"),
                        intensity = WorkoutIntensity.fromString(obj.optString("intensity", "STANDARD")),
                        estimatedCaloriesPerRound = obj.optInt("estimatedCaloriesPerRound", 15),
                        isCustom = true
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    private fun savePresetsList(list: List<WorkoutRoutine>) {
        val jsonArray = JSONArray()
        for (item in list) {
            val obj = JSONObject()
            obj.put("id", item.id)
            obj.put("title", item.title)
            obj.put("animeSeries", item.animeSeries)
            obj.put("description", item.description)
            obj.put("prepSeconds", item.prepSeconds)
            obj.put("workSeconds", item.workSeconds)
            obj.put("restSeconds", item.restSeconds)
            obj.put("totalRounds", item.totalRounds)
            obj.put("bossName", item.bossName)
            obj.put("difficulty", item.difficulty)
            obj.put("intensity", item.intensity.name)
            obj.put("estimatedCaloriesPerRound", item.estimatedCaloriesPerRound)
            jsonArray.put(obj)
        }
        prefs.edit().putString("custom_routines_json", jsonArray.toString()).apply()
    }
}
