package com.example.meetapp.data.search

import com.example.meetapp.network.MarvelService

class SearchRepository(
    private val service: MarvelService
) {

    fun getCharacters(
        limit: Int,
        offset: Int,
        query: String
    ) = service.getCharacters(
        limit = limit,
        offset = offset,
        query = query
    )
        .map { response -> response.data.results }
        .map { characters -> characters.map { it.toDomain() } }
        .doAfterSuccess {  }

}