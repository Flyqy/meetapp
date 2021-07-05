package com.example.meetapp.stuff.recycler

import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.meetapp.stuff.findKeyByValue

@Suppress("UNUSED")
open class AdapterDelegatesManager<T> {

    protected var delegates = SparseArrayCompat<AdapterDelegate<T>>()

    fun addDelegate(delegate: AdapterDelegate<T>) {
        var viewType = delegates.size()
        while (delegates.get(viewType) != null) {
            viewType++
        }
        addDelegate(delegate, viewType)
    }

    fun addDelegate(delegate: AdapterDelegate<T>, viewType: Int) {
        if (delegates[viewType] != null) {
            throw IllegalStateException("Delegate already exist with viewType [$viewType]")
        }
        delegates.put(viewType, delegate)
    }

    fun removeDelegate(delegate: AdapterDelegate<T>) {
        val index = delegates.indexOfValue(delegate)
        if (index >= 0) {
            delegates.removeAt(index)
        }
    }

    fun removeDelegate(viewType: Int) {
        delegates.remove(viewType)
    }

    @CallSuper
    open fun getItemViewType(items: T, position: Int): Int {
        return requireNotNull(delegates.findKeyByValue { delegate ->
            delegate.isForViewType(items, position)
        }) {
            "Can't find view type at [$position]"
        }
    }

    @CallSuper
    open fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return requireDelegate(viewType).onCreateViewHolder(parent)
    }

    @CallSuper
    open fun onBindViewHolder(
        items: T,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: List<Any> = emptyList()
    ) {
        requireDelegate(holder).onBindViewHolder(items, position, holder, payloads)
    }

    @CallSuper
    open fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        requireDelegate(holder).onViewRecycled(holder)
    }

    @CallSuper
    open fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return requireDelegate(holder).onFailedToRecycleView(holder)
    }

    @CallSuper
    open fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        requireDelegate(holder).onViewAttachedToWindow(holder)
    }

    @CallSuper
    open fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        requireDelegate(holder).onViewDetachedFromWindow(holder)
    }

    fun getViewType(delegate: AdapterDelegate<T>): Int {
        val index = delegates.indexOfValue(delegate)
        return if (index == -1) -1 else delegates.keyAt(index)
    }

    fun getDelegateForViewType(viewType: Int): AdapterDelegate<T>? {
        return delegates.get(viewType)
    }

    private fun requireDelegate(holder: RecyclerView.ViewHolder): AdapterDelegate<T> {
        return requireNotNull(getDelegateForViewType(holder.itemViewType)) {
            "Unknown view type [${holder.itemViewType}] at [${holder.adapterPosition}]"
        }
    }

    private fun requireDelegate(viewType: Int): AdapterDelegate<T> {
        return requireNotNull(getDelegateForViewType(viewType)) {
            "No AdapterDelegate added for ViewType $viewType"
        }
    }

    fun delegateViewsTypes(): IntArray {
        val viewTypes = ArrayList<Int>()
        for (i: Int in 0 until delegates.size()) {
            viewTypes.add(i)
        }
        return viewTypes.toIntArray()
    }

    fun findViewTypeByDelegate(predicate: (AdapterDelegate<T>) -> Boolean): Int = delegates
        .findKeyByValue(predicate) ?: -1
}
