package com.tunepruner.musictraining

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.example.musictraining.R

class MetronomeClicker(
    androidContext: Context,
) {
    private val audioAttributes =
        AudioAttributes
            .Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
    private val soundpool: SoundPool =
        SoundPool
            .Builder()
            .setMaxStreams(3)
            .setAudioAttributes(audioAttributes)
            .build()

    private val sound1 = soundpool.load(androidContext, R.raw.loud_knock, 1)
    private val sound2 = soundpool.load(androidContext, R.raw.soft_knock, 1)

    fun playLoudSound() {
        soundpool.play(sound1, 1F, 1F, 1, 0, 1F)
    }
    fun playSoftSound() {
        soundpool.play(sound2, 1F, 1F, 1, 0, 1F)
    }
}