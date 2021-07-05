package com.example.meetapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CharacterEntity::class],
    version = 1, exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun charactersDao(): CharacterDao
}