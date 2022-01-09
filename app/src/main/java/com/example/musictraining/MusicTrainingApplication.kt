package com.example.musictraining

import android.app.Application
import org.koin.core.context.startKoin
import org.koin.dsl.module

//val miscModule = module {
//    single { (ChordViewModel(get())) }
//}
//
//val repositoryModule = module {
//    single { SettingsRepository() }
//}
//
//val viewModelModule = module {
//    single { FirstViewModel(get(),get()) }
//}
//
//class MusicTrainingApplication : Application() {
//    override fun onCreate() {
//        super.onCreate()
//        startKoin {
//            modules(mutableListOf(
//                miscModule,
//                repositoryModule,
//                viewModelModule
//            ))
//        }
//    }
//}