package com.example.meetapp.stuff.recycler.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.DimenRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meetapp.R

class CustomDividerItemDecoration(
    private val context: Context,
    viewTypes: IntArray,
    @DimenRes private val leftPaddingDivider: Int = 0,
    @DimenRes private val rightPaddingDivider: Int = 0,
    @DimenRes edgeLeft: Int = -1,
    @DimenRes edgeTop: Int = -1,
    @DimenRes edgeRight: Int = -1,
    @DimenRes edgeBottom: Int = -1
) : BaseItemDecoration(
    viewTypes,
    if (edgeLeft != -1) context.resources.getDimensionPixelOffset(edgeLeft) else 0,
    if (edgeTop != -1) context.resources.getDimensionPixelOffset(edgeTop) else 0,
    if (edgeRight != -1) context.resources.getDimensionPixelOffset(edgeRight) else 0,
    if (edgeBottom != -1) context.resources.getDimensionPixelOffset(edgeBottom) else 0
) {

    constructor(
        context: Context,
        viewTypes: IntArray,
        @DimenRes paddingDivider: Int = 0,
        @DimenRes verticalSpacing: Int = -1,
        @DimenRes horizontalSpacing: Int = -1
    ) : this(
        context,
        viewTypes,
        paddingDivider,
        paddingDivider,
        horizontalSpacing,
        verticalSpacing,
        horizontalSpacing,
        verticalSpacing
    )

    private val divider = AppCompatResources.getDrawable(context, R.drawable.line_divider)!!

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val adapter = parent.adapter

        if (adapter != null) {
            val childCount = parent.childCount
            for (i in 0 until childCount - 1) {
                drawDivider(parent, i, adapter, c)
            }
        }
    }

    private fun drawDivider(
        parent: RecyclerView,
        index: Int,
        adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
        canvas: Canvas
    ) {
        val child = parent.getChildAt(index)
        val childViewHolder = parent.getChildViewHolder(child)
        val position = childViewHolder.adapterPosition
        if (position != RecyclerView.NO_POSITION && position + 1 < adapter.itemCount) {
            val viewType = childViewHolder.itemViewType
            val nextViewType = adapter.getItemViewType(position + 1)
            if (isDecoratedItem(viewType) && isDecoratedItem(nextViewType)) {
                val params = child.layoutParams as RecyclerView.LayoutParams
                val left = if (leftPaddingDivider != 0) {
                    context.resources.getDimensionPixelOffset(leftPaddingDivider)
                } else leftPaddingDivider
                val right = parent.width - if (rightPaddingDivider != 0) {
                    context.resources.getDimensionPixelOffset(rightPaddingDivider)
                } else rightPaddingDivider
                val top = child.bottom + params.bottomMargin
                val bottom = top + divider.intrinsicHeight

                divider.setBounds(left, top, right, bottom)
                divider.draw(canvas)
            }
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val manager = parent.layoutManager as LinearLayoutManager
        val adapterPosition = parent.getChildViewHolder(view).adapterPosition
        val adapter = parent.adapter

        if (adapter != null && adapterPosition != RecyclerView.NO_POSITION) {
            val indexLastItem = adapter.itemCount - 1
            if (manager.orientation == LinearLayout.VERTICAL) {
                val itemViewHolder = adapter.getItemViewType(adapterPosition)
                if (isDecoratedItem(itemViewHolder)) {
                    horizontalEdgeSpacing(outRect)
                    verticalFirstAndLastItemSpacing(
                        outRect,
                        adapterPosition,
                        indexLastItem,
                        adapter
                    )
                }
            }
        }
    }
}
