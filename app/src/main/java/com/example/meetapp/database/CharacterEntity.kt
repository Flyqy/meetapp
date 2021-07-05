package com.example.meetapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
class CharacterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val name: String?,
    val description: String?,
    val logo: String?
)