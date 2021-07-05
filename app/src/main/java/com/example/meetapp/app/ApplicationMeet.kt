package com.example.meetapp.app

import android.app.Application
import com.example.meetapp.database.AppDatabase
import com.example.meetapp.database.CharacterDao
import com.example.meetapp.di.business.Business
import com.example.meetapp.di.navigation.Navigation
import com.example.meetapp.di.network.Network
import com.example.meetapp.di.root.Root
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class ApplicationMeet : Application() {

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@ApplicationMeet)
            modules(
                listOf(
                    Business.databaseModule,
                    Navigation.navigationModule,
                    Network.networkModule,
                    Root.rootModule,
                    Root.mainModule,
                    Business.businessModule
                )
            )
        }
    }
}