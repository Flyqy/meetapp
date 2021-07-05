package com.example.meetapp.di.root

import com.example.meetapp.features.search.SearchPresenter
import com.example.meetapp.features.root.RootPresenter
import org.koin.dsl.module

object Root {

    val rootModule = module {
        single { RootPresenter(get()) }
    }

    val mainModule = module {
        single { SearchPresenter(get(), get()) }
    }
}