package com.example.meetapp.database

import androidx.room.*

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCharacter(value: CharacterEntity)

    @Query("SELECT * FROM characters")
    fun getCharacters(): List<CharacterEntity>
}