package com.example.meetapp.stuff.recycler.decoration

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView

abstract class BaseItemDecoration(
    private val viewTypes: IntArray,
    private val edgeLeft: Int,
    private val edgeTop: Int,
    private val edgeRight: Int,
    private val edgeBottom: Int
) : RecyclerView.ItemDecoration() {

    fun isDecoratedItem(holderViewType: Int) = holderViewType in viewTypes

    fun verticalFirstAndLastItemSpacing(
        outRect: Rect,
        adapterPosition: Int,
        lastItemIndex: Int,
        adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    ) {
        if (adapterPosition in 1 until lastItemIndex) {
            val previousItemViewType = adapter.getItemViewType(adapterPosition - 1)
            if (!isDecoratedItem(previousItemViewType)) verticalSetSpaceBeforeFirstItem(outRect)
            val nextItemViewType = adapter.getItemViewType(adapterPosition + 1)
            if (!isDecoratedItem(nextItemViewType)) verticalSetSpaceAfterLastItem(outRect)
        } else {
            when (adapterPosition) {
                0 -> verticalSetSpaceBeforeFirstItem(outRect)
                lastItemIndex -> {
                    if (adapterPosition > 0) {
                        val previousItemViewType = adapter.getItemViewType(adapterPosition - 1)
                        if (!isDecoratedItem(previousItemViewType)) verticalSetSpaceBeforeFirstItem(outRect)
                    }
                    verticalSetSpaceAfterLastItem(outRect)
                }
            }
        }
    }

    fun horizontalEdgeSpacing(outRect: Rect) {
        outRect.left = edgeLeft
        outRect.right = edgeRight
    }

    fun horizontalFirstAndLastItemSpacing(
        outRect: Rect,
        adapterPosition: Int,
        lastItemIndex: Int,
        adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    ) {
        if (adapterPosition in 1 until lastItemIndex) {
            val previousItemViewType = adapter.getItemViewType(adapterPosition - 1)
            if (!isDecoratedItem(previousItemViewType)) horizontalSetSpaceBeforeFirstItem(outRect)
            val nextItemViewType = adapter.getItemViewType(adapterPosition + 1)
            if (!isDecoratedItem(nextItemViewType)) horizontalSetSpaceAfterLastItem(outRect)
        } else {
            when (adapterPosition) {
                0 -> horizontalSetSpaceBeforeFirstItem(outRect)
                lastItemIndex -> horizontalSetSpaceAfterLastItem(outRect)
            }
        }
    }

    fun verticalEdgeSpacing(outRect: Rect) {
        outRect.bottom = edgeBottom
        outRect.top = edgeTop
    }

    private fun verticalSetSpaceBeforeFirstItem(outRect: Rect) {
        outRect.top = edgeTop
    }

    private fun verticalSetSpaceAfterLastItem(outRect: Rect) {
        outRect.bottom = edgeBottom
    }

    private fun horizontalSetSpaceBeforeFirstItem(outRect: Rect) {
        outRect.left = edgeLeft
    }

    private fun horizontalSetSpaceAfterLastItem(outRect: Rect) {
        outRect.right = edgeRight
    }
}
