package com.example.meetapp.features.screens

import com.example.meetapp.features.search.SearchFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {

    fun SearchScreen() = FragmentScreen(
        key = SearchFragment.MAIN_SCREEN,
        fragmentCreator = SearchFragment.newInstance()
    )
}