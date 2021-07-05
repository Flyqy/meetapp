package com.example.meetapp.stuff.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class EasyDelegate<B : Any, T : B, Holder : ViewHolder> : AdapterDelegate<List<B>>() {

    final override fun onBindViewHolder(
        items: List<B>,
        position: Int,
        holder: ViewHolder,
        payloads: List<Any>
    ) {
        @Suppress("UNCHECKED_CAST")
        onBindViewHolder(items[position] as T, holder as Holder, payloads)
        onPostBind(items, position, holder, payloads)
    }

    final override fun isForViewType(items: List<B>, position: Int): Boolean {
        return isForViewType(items[position])
    }

    abstract fun isForViewType(item: B): Boolean

    abstract override fun onCreateViewHolder(parent: ViewGroup): Holder

    abstract fun onBindViewHolder(item: T, holder: Holder, payloads: List<Any>)
    protected open fun onPostBind(items: List<B>, position: Int, holder: Holder, payloads: List<Any>) = Unit
}
