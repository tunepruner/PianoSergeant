package com.tunepruner.musictraining

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.musictraining.R

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_main)

        val audioAttributes =
            AudioAttributes
                .Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        val soundpool: SoundPool =
            SoundPool
                .Builder()
                .setMaxStreams(3)
                .setAudioAttributes(audioAttributes)
                .build()
    }

}