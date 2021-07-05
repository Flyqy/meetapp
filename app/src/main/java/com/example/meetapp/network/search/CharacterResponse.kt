package com.example.meetapp.network.search

import com.example.meetapp.models.Character
import com.google.gson.annotations.SerializedName

class CharacterResponse(
    @SerializedName("results") val results: List<CharacterInfo>?
)

class CharacterInfo(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("thumbnail") val characterLogo: CharacterLogo?
) {

    fun toDomain() = Character(
        id = id,
        name = name,
        description = description,
        logo = characterLogo?.let { "${it.path}.${it.extension}".replace("http", "https", true) }
    )
}

class CharacterLogo(
    @SerializedName("path") val path: String?,
    @SerializedName("extension") val extension: String?
)