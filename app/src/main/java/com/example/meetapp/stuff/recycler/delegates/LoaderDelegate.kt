package com.example.meetapp.stuff.recycler.delegates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meetapp.R
import com.example.meetapp.stuff.recycler.EasyDelegate

class LoaderDelegate : EasyDelegate<Any, LoaderItem, LoaderViewHolder>() {

    override fun isForViewType(item: Any): Boolean = item is LoaderItem

    override fun onCreateViewHolder(parent: ViewGroup): LoaderViewHolder =
        LoaderViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_loader, parent, false)
        )

    override fun onBindViewHolder(
        item: LoaderItem,
        holder: LoaderViewHolder,
        payloads: List<Any>
    ) = Unit
}

class LoaderViewHolder(view: View) : RecyclerView.ViewHolder(view)

object LoaderItem
