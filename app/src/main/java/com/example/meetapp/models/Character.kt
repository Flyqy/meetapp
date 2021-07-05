package com.example.meetapp.models

import com.example.meetapp.features.search.CharacterUi

data class Character(
    val id: Long,
    val name: String?,
    val description: String?,
    val logo: String?
) {

    fun toUi() = CharacterUi(
        id = id,
        name = name,
        description = description,
        logo = logo
    )
}