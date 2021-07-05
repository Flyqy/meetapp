package com.example.meetapp.stuff.recycler.delegates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meetapp.R
import com.example.meetapp.stuff.recycler.EasyDelegate

class PageLoadingDelegate : EasyDelegate<Any, PageLoadingItem, PageLoadingViewHolder>() {

    override fun isForViewType(item: Any): Boolean = item is PageLoadingItem

    override fun onCreateViewHolder(parent: ViewGroup): PageLoadingViewHolder =
        PageLoadingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_page_loading_vertical, parent, false))

    override fun onBindViewHolder(
        item: PageLoadingItem,
        holder: PageLoadingViewHolder,
        payloads: List<Any>
    ) = Unit
}

class PageLoadingViewHolder(view: View) : RecyclerView.ViewHolder(view)

object PageLoadingItem
