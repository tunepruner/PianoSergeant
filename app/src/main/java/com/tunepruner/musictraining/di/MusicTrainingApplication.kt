package com.tunepruner.musictraining.di

import android.app.Application
import com.tunepruner.musictraining.model.constants.dataStore
import com.tunepruner.musictraining.model.music.AttemptProcessor
import com.tunepruner.musictraining.repositories.IncomingMidiSource
import com.tunepruner.musictraining.repositories.DrillSettingsRepository
import com.tunepruner.musictraining.util.MetronomeClicker
import com.tunepruner.musictraining.viewmodel.ChordDrillSettingsViewModel
import com.tunepruner.musictraining.viewmodel.ChordViewModel
import com.tunepruner.musictraining.viewmodel.DrillListViewModel
import com.tunepruner.musictraining.viewmodel.MetronomeViewModel
import com.tunepruner.musictraining.viewmodel.NoteInputViewModel
import com.tunepruner.musictraining.viewmodel.ScaleDrillSettingsViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

val miscModule = module {
    single { (ChordViewModel(get())) }
    single { AttemptProcessor() }
    single { IncomingMidiSource(androidContext()) }
}

val repositoryModule = module {
    single { DrillSettingsRepository(androidContext(), androidContext().dataStore) }
}

val soundModule = module {
    single {
        MetronomeClicker(androidContext())
    }
}

@InternalCoroutinesApi
val viewModelModule = module {
    single { MetronomeViewModel(get(), get()) }
    single { ChordViewModel(get()) }
    single { NoteInputViewModel(get(), androidContext()) }
    single { ChordDrillSettingsViewModel(get()) }
    single { ScaleDrillSettingsViewModel(get()) }
    single { DrillListViewModel(get()) }
}

    @InternalCoroutinesApi
class MusicTrainingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MusicTrainingApplication)
            modules(
                mutableListOf(
                    soundModule,
                    miscModule,
                    repositoryModule,
                    viewModelModule,
                )
            )
        }
    }

}