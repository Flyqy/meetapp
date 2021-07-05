package com.example.meetapp.di.navigation

import com.github.terrakok.cicerone.Cicerone
import org.koin.dsl.module

object Navigation {
    private val cicerone = Cicerone.create()
    private val router = cicerone.router
    private val navigationHolder = cicerone.getNavigatorHolder()

    val navigationModule = module {
        single { router }
        single { navigationHolder }
    }
}