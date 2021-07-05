package com.example.meetapp.features.search

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SearchView : MvpView {

    fun updateItems(data: List<CharacterUi>)
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showEmpty()
    fun showLoader()
    fun showPageProgress()
    fun showError()
}