package com.example.meetapp.stuff.recycler

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlin.math.abs

class PagingListener(
    private val pageSize: Int,
    private val onLastPageReached: () -> Unit
) : OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val position = getLastVisibleItemPosition(recyclerView)
        val itemsCount = recyclerView.adapter?.itemCount ?: throw IllegalStateException()
        val updatePosition = abs(itemsCount - 1 - pageSize / 2)
        if (position >= updatePosition) {
            onLastPageReached()
        }
    }

    private fun getLastVisibleItemPosition(recyclerView: RecyclerView): Int {
        val layoutManager = recyclerView.layoutManager
        return when (layoutManager) {
            is LinearLayoutManager -> layoutManager.findLastVisibleItemPosition()
            is StaggeredGridLayoutManager -> layoutManager.findLastVisibleItemPositions(null).max() ?: 0
            else -> throw IllegalStateException("Implement findLastVisibleItemPosition for $layoutManager")
        }
    }
}
