package com.tunepruner.musictraining

import android.app.Application
import com.example.musictraining.R
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

val miscModule = module {
    single { (ChordViewModel(get())) }
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
}

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
                    viewModelModule
                )
            )
        }
    }

}