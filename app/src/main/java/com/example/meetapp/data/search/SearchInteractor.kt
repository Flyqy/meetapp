package com.example.meetapp.data.search

class SearchInteractor(
    private val searchRepository: SearchRepository
) {

    fun getCharacters(
        limit: Int,
        offset: Int,
        query: String
    ) = searchRepository.getCharacters(limit, offset, query)
}