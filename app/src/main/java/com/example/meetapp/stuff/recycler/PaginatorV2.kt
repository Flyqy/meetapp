package com.example.meetapp.stuff.recycler

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

open class PaginatorV2<T>(
    private val requestFactory: (Int) -> Single<List<T>>,
    private val viewController: ViewController<T>
) {

    interface ViewController<T> {
        fun showEmptyProgress()
        fun hideEmptyProgress()
        fun showEmptyView()
        fun hideEmptyView()
        fun showEmptyError(error: Throwable)
        fun hideEmptyError()
        fun showRefreshProgress()
        fun hideRefreshProgress()
        fun showPageLoadingProgress()
        fun hidePageLoadingProgress()
        fun hideData()
        fun showData(data: List<T>)
        fun showRefreshError(error: Throwable)
        fun showPageLoadingError(error: Throwable)
        fun hidePageLoadingError()
        fun endLoadingAllData(data: List<T>)
    }

    open class EmptyViewController<T> : ViewController<T> {
        override fun showEmptyProgress() = Unit
        override fun hideEmptyProgress() = Unit
        override fun showEmptyView() = Unit
        override fun hideEmptyView() = Unit
        override fun showEmptyError(error: Throwable) = Unit
        override fun hideEmptyError() = Unit
        override fun showRefreshProgress() = Unit
        override fun hideRefreshProgress() = Unit
        override fun showPageLoadingProgress() = Unit
        override fun hidePageLoadingProgress() = Unit
        override fun hideData() = Unit
        override fun showData(data: List<T>) = Unit
        override fun showRefreshError(error: Throwable) = Unit
        override fun showPageLoadingError(error: Throwable) = Unit
        override fun hidePageLoadingError() = Unit
        override fun endLoadingAllData(data: List<T>) = Unit
    }

    private val FIRST_PAGE = 1

    protected var currentData = emptyList<T>()

    private var currentState: State<T> = EMPTY()
    private var currentPage = 0
    private var disposable: Disposable? = null

    protected open val collisionsFilter: ((List<T>) -> List<T>)? = null

    fun restart() {
        currentState.restart()
    }

    fun refresh() {
        currentState.refresh()
    }

    fun loadNewPage() {
        currentState.loadNewPage()
    }

    fun release() {
        currentState.release()
    }

    protected fun setPage(page: Int) {
        currentPage = page
    }

    private fun loadPage(page: Int) {
        disposable?.dispose()
        disposable = requestFactory.invoke(page)
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                currentState::newData,
                currentState::fail
            )
    }

    private interface State<T> {
        fun restart() {}
        fun refresh() {}
        fun loadNewPage() {}
        fun release() {}
        fun newData(data: List<T>) {}
        fun fail(error: Throwable) {}
    }

    private inner class EMPTY : State<T> {

        override fun refresh() {
            currentState = EMPTY_PROGRESS()
            viewController.showEmptyProgress()
            loadPage(FIRST_PAGE)
        }

        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.showEmptyProgress()
            loadPage(FIRST_PAGE)
        }

        override fun release() {
            currentState = RELEASED()
            disposable?.dispose()
        }
    }

    private inner class EMPTY_PROGRESS : State<T> {

        override fun restart() {
            loadPage(FIRST_PAGE)
        }

        override fun newData(data: List<T>) {
            if (data.isNotEmpty()) {
                currentState = DATA()
                currentPage = FIRST_PAGE
                currentData = data
                viewController.hideEmptyProgress()
                viewController.showData(currentData)
            } else {
                currentState = EMPTY_DATA()
                viewController.hideEmptyProgress()
                viewController.showEmptyView()
            }
        }

        override fun fail(error: Throwable) {
            currentState = EMPTY_ERROR()
            viewController.hideEmptyProgress()
            viewController.showEmptyError(error)
        }

        override fun release() {
            currentState = RELEASED()
            disposable?.dispose()
        }
    }

    private inner class EMPTY_ERROR : State<T> {

        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.hideEmptyError()
            viewController.showEmptyProgress()
            loadPage(FIRST_PAGE)
        }

        override fun refresh() {
            currentState = EMPTY_PROGRESS()
            viewController.hideEmptyError()
            viewController.showEmptyProgress()
            loadPage(FIRST_PAGE)
        }

        override fun release() {
            currentState = RELEASED()
            disposable?.dispose()
        }
    }

    private inner class EMPTY_DATA : State<T> {

        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.hideEmptyView()
            viewController.showEmptyProgress()
            loadPage(FIRST_PAGE)
        }

        override fun refresh() {
            currentState = EMPTY_PROGRESS()
            viewController.hideEmptyView()
            viewController.showEmptyProgress()
            loadPage(FIRST_PAGE)
        }

        override fun release() {
            currentState = RELEASED()
            disposable?.dispose()
        }
    }

    private inner class DATA : State<T> {

        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.hideData()
            viewController.showEmptyProgress()
            loadPage(FIRST_PAGE)
        }

        override fun refresh() {
            currentState = REFRESH()
            viewController.showRefreshProgress()
            loadPage(FIRST_PAGE)
        }

        override fun loadNewPage() {
            currentState = PAGE_PROGRESS()
            viewController.showPageLoadingProgress()
            loadPage(currentPage + 1)
        }

        override fun release() {
            currentState = RELEASED()
            disposable?.dispose()
        }
    }

    private inner class REFRESH : State<T> {

        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.hideData()
            viewController.hideRefreshProgress()
            viewController.showEmptyProgress()
            loadPage(FIRST_PAGE)
        }

        override fun refresh() {
            viewController.showRefreshProgress()
            loadPage(FIRST_PAGE)
        }

        override fun newData(data: List<T>) {
            if (data.isNotEmpty()) {
                currentState = DATA()
                currentPage = FIRST_PAGE
                currentData = data
                viewController.hideRefreshProgress()
                viewController.showData(currentData)
            } else {
                currentState = EMPTY_DATA()
                viewController.hideData()
                viewController.hideRefreshProgress()
                viewController.showEmptyView()
            }
        }

        override fun fail(error: Throwable) {
            currentState = DATA()
            viewController.hideRefreshProgress()
            viewController.showRefreshError(error)
        }

        override fun release() {
            currentState = RELEASED()
            disposable?.dispose()
        }
    }

    private inner class PAGE_PROGRESS : State<T> {

        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.hideData()
            viewController.hidePageLoadingProgress()
            viewController.showEmptyProgress()
            loadPage(FIRST_PAGE)
        }

        override fun newData(data: List<T>) {
            if (data.isNotEmpty()) {
                val newData = collisionsFilter?.let { func -> func(data) } ?: data
                currentState = DATA()
                currentPage++
                currentData = currentData.plus(newData)
                viewController.showData(currentData)
                viewController.hidePageLoadingProgress()
            } else {
                currentState = ALL_DATA()
                viewController.hidePageLoadingProgress()
                viewController.endLoadingAllData(data)
            }
        }

        override fun refresh() {
            currentState = REFRESH()
            viewController.hidePageLoadingProgress()
            viewController.showRefreshProgress()
            loadPage(FIRST_PAGE)
        }

        override fun fail(error: Throwable) {
            currentState = PAGE_ERROR()
            viewController.hidePageLoadingProgress()
            viewController.showPageLoadingError(error)
        }

        override fun release() {
            currentState = RELEASED()
            disposable?.dispose()
        }
    }

    private inner class PAGE_ERROR : State<T> {
        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.hideData()
            viewController.hidePageLoadingError()
            viewController.showEmptyProgress()
            loadPage(FIRST_PAGE)
        }

        override fun refresh() {
            currentState = REFRESH()
            viewController.hidePageLoadingError()
            viewController.showRefreshProgress()
            loadPage(FIRST_PAGE)
        }

        override fun loadNewPage() {
            currentState = PAGE_PROGRESS()
            viewController.hidePageLoadingError()
            viewController.showPageLoadingProgress()
            loadPage(currentPage + 1)
        }

        override fun release() {
            currentState = RELEASED()
            disposable?.dispose()
        }
    }

    private inner class ALL_DATA : State<T> {

        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.hideData()
            viewController.showEmptyProgress()
            loadPage(FIRST_PAGE)
        }

        override fun refresh() {
            currentState = REFRESH()
            viewController.showRefreshProgress()
            loadPage(FIRST_PAGE)
        }

        override fun release() {
            currentState = RELEASED()
            disposable?.dispose()
        }
    }

    private inner class RELEASED : State<T>
}
