package com.example.meetapp.di.business

import android.app.Application
import androidx.room.Room
import com.example.meetapp.data.search.SearchInteractor
import com.example.meetapp.data.search.SearchRepository
import com.example.meetapp.database.AppDatabase
import com.example.meetapp.database.CharacterDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

object Business {

    val businessModule = module {
        single { SearchInteractor(get()) }
        single { SearchRepository(get()) }
    }

    val databaseModule = module {

        fun provideDatabase(application: Application): AppDatabase {
            return Room.databaseBuilder(application, AppDatabase::class.java, "characters")
                .fallbackToDestructiveMigration()
                .build()
        }

        fun provideCharacterDao(database: AppDatabase): CharacterDao {
            return database.charactersDao()
        }

        single { provideDatabase(androidApplication()) }
        single { provideCharacterDao(get()) }
    }
}