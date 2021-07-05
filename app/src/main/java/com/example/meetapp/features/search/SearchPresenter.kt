package com.example.meetapp.features.search

import com.example.meetapp.base.presenter.BasePresenter
import com.example.meetapp.data.search.SearchInteractor
import com.example.meetapp.stuff.recycler.PaginatorV2
import com.github.terrakok.cicerone.Router
import moxy.InjectViewState

@InjectViewState
class SearchPresenter(
    private val fragmentRouter: Router,
    private val searchInteractor: SearchInteractor
) : BasePresenter<SearchView>() {

    private var query: String = String()

    private val paginator = PaginatorV2(
        ::requestPage,
        object : PaginatorV2.EmptyViewController<CharacterUi>() {
            override fun showData(data: List<CharacterUi>) = viewState.updateItems(data)
            override fun showEmptyView() = viewState.showEmpty()
            override fun showEmptyProgress() = viewState.showLoader()
            override fun showRefreshProgress() = viewState.showLoader()
            override fun showPageLoadingProgress() = viewState.showPageProgress()
            override fun showPageLoadingError(error: Throwable) = viewState.showError()
            override fun showRefreshError(error: Throwable) = viewState.showError()
        }
    )

    init {

    }

    fun onQueryChange(value: String) {
        if (value.length > 2) {
            query = value
            paginator.refresh()
        } else {
            query = String()
        }
    }

    fun onRetryClick() = paginator.restart()

    private fun requestPage(
        page: Int
    ) = searchInteractor.getCharacters(
        PAGE_SIZE,
        PAGE_SIZE * (page - 1),
        query
    )
        .map { characters -> characters.map { character -> character.toUi() } }
        .doOnError { viewState.showError() }

    fun onEndReached() = paginator.loadNewPage()

    companion object {
        const val PAGE_SIZE = 10
    }
}