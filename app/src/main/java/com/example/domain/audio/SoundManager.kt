package com.example.domain.audio

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SoundManager private constructor(context: Context) {

    private val appContext = context.applicationContext
    private var toneGen: ToneGenerator? = null
    private var vibrator: Vibrator? = null

    var isSoundEnabled: Boolean = true
    var isMusicEnabled: Boolean = true
    var isHapticEnabled: Boolean = true

    init {
        try {
            toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, 80)
        } catch (e: Exception) {
            toneGen = null
        }

        try {
            vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = appContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vm?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                appContext.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }
        } catch (e: Exception) {
            vibrator = null
        }
    }

    fun playButtonClick() {
        if (!isSoundEnabled) return
        try {
            toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP, 40)
        } catch (_: Exception) {}
        vibrateShort(20)
    }

    fun playPieceSnap() {
        if (!isSoundEnabled) return
        try {
            toneGen?.startTone(ToneGenerator.TONE_PROP_PROMPT, 70)
        } catch (_: Exception) {}
        vibrateShort(35)
    }

    fun playWrongMove() {
        if (!isSoundEnabled) return
        try {
            toneGen?.startTone(ToneGenerator.TONE_PROP_NACK, 120)
        } catch (_: Exception) {}
        vibratePattern(longArrayOf(0, 50, 50, 50))
    }

    fun playCountdownTick() {
        if (!isSoundEnabled) return
        try {
            toneGen?.startTone(ToneGenerator.TONE_CDMA_PIP, 80)
        } catch (_: Exception) {}
        vibrateShort(30)
    }

    fun playCountdownGo() {
        if (!isSoundEnabled) return
        try {
            toneGen?.startTone(ToneGenerator.TONE_CDMA_ALERT_NETWORK_LITE, 250)
        } catch (_: Exception) {}
        vibrateShort(80)
    }

    fun playVictory() {
        if (!isSoundEnabled) return
        CoroutineScope(Dispatchers.Default).launch {
            try {
                toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP2, 100)
                delay(120)
                toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP2, 100)
                delay(120)
                toneGen?.startTone(ToneGenerator.TONE_PROP_PROMPT, 300)
            } catch (_: Exception) {}
        }
        vibratePattern(longArrayOf(0, 100, 100, 200))
    }

    fun playDefeat() {
        if (!isSoundEnabled) return
        try {
            toneGen?.startTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE, 300)
        } catch (_: Exception) {}
        vibrateShort(150)
    }

    fun playAchievementUnlocked() {
        if (!isSoundEnabled) return
        CoroutineScope(Dispatchers.Default).launch {
            try {
                toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP, 80)
                delay(100)
                toneGen?.startTone(ToneGenerator.TONE_PROP_PROMPT, 200)
            } catch (_: Exception) {}
        }
        vibratePattern(longArrayOf(0, 50, 50, 100))
    }

    private fun vibrateShort(durationMs: Long) {
        if (!isHapticEnabled) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(durationMs)
            }
        } catch (_: Exception) {}
    }

    private fun vibratePattern(pattern: LongArray) {
        if (!isHapticEnabled) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(pattern, -1)
            }
        } catch (_: Exception) {}
    }

    companion object {
        @Volatile
        private var INSTANCE: SoundManager? = null

        fun getInstance(context: Context): SoundManager {
            return INSTANCE ?: synchronized(this) {
                val instance = SoundManager(context)
                INSTANCE = instance
                instance
            }
        }
    }
}
