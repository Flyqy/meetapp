package com.example.meetapp.network

import com.example.meetapp.network.search.CharacterResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelService {

    @GET("characters")
    fun getCharacters(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("orderBy") orderBy: String = "name",
        @Query("nameStartsWith") query: String
    ): Single<Response<CharacterResponse>>
}