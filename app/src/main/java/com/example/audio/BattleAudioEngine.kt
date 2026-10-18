package com.example.audio

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import java.util.Locale

class BattleAudioEngine(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isTtsReady = false
    private var toneGenerator: ToneGenerator? = null
    private var isMuted = false
    private var isVoiceEnabled = true

    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        } catch (e: Exception) {
            Log.e("BattleAudioEngine", "ToneGenerator init failed", e)
        }
        tts = TextToSpeech(context.applicationContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.w("BattleAudioEngine", "US English not supported for TTS")
            } else {
                isTtsReady = true
                tts?.setSpeechRate(1.1f) // Energetic anime cadence
                tts?.setPitch(1.05f)
            }
        }
    }

    fun setMuted(muted: Boolean) {
        this.isMuted = muted
    }

    fun setVoiceEnabled(enabled: Boolean) {
        this.isVoiceEnabled = enabled
        if (!enabled) {
            tts?.stop()
        }
    }

    fun speak(text: String, priority: Boolean = false) {
        if (isMuted || !isVoiceEnabled || !isTtsReady) return
        val queueMode = if (priority) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD
        tts?.speak(text, queueMode, null, "utterance_${System.currentTimeMillis()}")
    }

    fun playCountdownBeep(secondsLeft: Int) {
        if (isMuted) return
        try {
            when (secondsLeft) {
                3, 2 -> {
                    toneGenerator?.startTone(ToneGenerator.TONE_CDMA_PIP, 120)
                    vibrate(70)
                }
                1 -> {
                    toneGenerator?.startTone(ToneGenerator.TONE_CDMA_PIP, 180)
                    vibrate(120)
                }
                0 -> {
                    // Start of phase
                    toneGenerator?.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 350)
                    vibrate(250)
                }
            }
        } catch (e: Exception) {
            Log.e("BattleAudioEngine", "Tone playback error", e)
        }
    }

    fun playVictoryFanfare() {
        if (isMuted) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_CDMA_HIGH_L, 500)
            vibrate(400)
        } catch (e: Exception) {
            Log.e("BattleAudioEngine", "Victory tone error", e)
        }
    }

    private fun vibrate(durationMillis: Long) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(durationMillis, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(durationMillis)
            }
        } catch (e: Exception) {
            Log.e("BattleAudioEngine", "Vibrate error", e)
        }
    }

    fun release() {
        try {
            tts?.stop()
            tts?.shutdown()
            toneGenerator?.release()
        } catch (e: Exception) {
            Log.e("BattleAudioEngine", "Release error", e)
        }
    }
}
