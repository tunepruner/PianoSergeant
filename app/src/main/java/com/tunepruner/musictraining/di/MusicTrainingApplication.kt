package com.tunepruner.musictraining.di

import android.app.Application
import com.tunepruner.musictraining.viewmodel.ChordViewModel
import com.tunepruner.musictraining.util.MetronomeClicker
import com.tunepruner.musictraining.repositories.SettingsRepository
import com.tunepruner.musictraining.model.music.AttemptProcessor
import com.tunepruner.musictraining.repositories.IncomingMidiSource
import com.tunepruner.musictraining.viewmodel.MetronomeViewModel
import com.tunepruner.musictraining.viewmodel.NoteInputViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

val miscModule = module {
    single { (ChordViewModel(get())) }
    single { AttemptProcessor() }
    single { IncomingMidiSource(androidContext()) }
}

val repositoryModule = module {
    single { SettingsRepository() }
}

val soundModule = module {
    single {
        MetronomeClicker(androidContext())
    }
}

val viewModelModule = module {
    single { MetronomeViewModel(get(), get()) }
    single { ChordViewModel(get()) }
    single { NoteInputViewModel(get()) }
}

class
MusicTrainingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MusicTrainingApplication)
            modules(
                mutableListOf(
                    soundModule,
                    miscModule,
                    repositoryModule,
                    viewModelModule
                )
            )
        }
    }

}