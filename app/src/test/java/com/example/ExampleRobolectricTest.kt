package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.AnimeQuotesRepository
import com.example.data.WorkoutHistoryRepository
import com.example.data.WorkoutPresetRepository
import com.example.model.WorkoutIntensity
import com.example.model.WorkoutPhase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Anime Workout", appName)
    }

    @Test
    fun `anime quotes repository returns quotes for all phases`() {
        val workQuote = AnimeQuotesRepository.getRandomQuoteForPhase(WorkoutPhase.WORK)
        val restQuote = AnimeQuotesRepository.getRandomQuoteForPhase(WorkoutPhase.REST)
        val victoryQuote = AnimeQuotesRepository.getRandomQuoteForPhase(WorkoutPhase.COMPLETED)

        assertNotNull(workQuote)
        assertNotNull(restQuote)
        assertNotNull(victoryQuote)
        assertTrue(workQuote.quote.isNotEmpty())
        assertTrue(workQuote.character.isNotEmpty())
    }

    @Test
    fun `default presets loaded properly with intensities`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val presetRepo = WorkoutPresetRepository(context)
        val presets = presetRepo.getAllPresets()

        assertTrue(presets.isNotEmpty())
        assertTrue(presets.any { it.intensity == WorkoutIntensity.LIMIT_BREAKER })
    }

    @Test
    fun `xp calculation scales with duration and intensity`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val historyRepo = WorkoutHistoryRepository(context)

        // 100 seconds casual vs 100 seconds limit breaker
        val casualXp = historyRepo.calculateXp(
            workDurationSeconds = 100,
            intensity = WorkoutIntensity.CASUAL,
            completedRounds = 5,
            totalRounds = 5,
            streakDays = 1
        )

        val limitBreakerXp = historyRepo.calculateXp(
            workDurationSeconds = 100,
            intensity = WorkoutIntensity.LIMIT_BREAKER,
            completedRounds = 5,
            totalRounds = 5,
            streakDays = 1
        )

        // Limit breaker must earn significantly more XP than casual
        assertTrue(limitBreakerXp.totalXpEarned > casualXp.totalXpEarned)
        assertEquals(100 * 4, casualXp.durationXp)
        assertEquals(0, casualXp.intensityBonusXp) // 1.0x has 0 bonus
        assertTrue(limitBreakerXp.intensityBonusXp > 0) // 3.0x has 200% bonus
    }

    @Test
    fun `user profile displays rank level and progress`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val historyRepo = WorkoutHistoryRepository(context)
        val profile = historyRepo.getUserProfile()

        assertTrue(profile.level >= 1)
        assertTrue(profile.rankTitle.isNotEmpty())
        assertTrue(profile.nextLevelTargetXp > profile.currentLevelMinXp)
        assertTrue(profile.progressFraction in 0f..1f)
    }
}
