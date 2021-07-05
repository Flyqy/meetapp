package com.example.meetapp.features.search.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.meetapp.features.search.CharacterUi
import com.example.meetapp.features.search.delegates.CharacterDelegate
import com.example.meetapp.stuff.recycler.PaginationAdapter

class SearchListAdapter : PaginationAdapter(CharacterDelegate()) {

    override val diffItemCallback: DiffUtil.ItemCallback<Any> = object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem is CharacterUi && newItem is CharacterUi -> {
                    oldItem.id == newItem.id
                }
                else -> {
                    false
                }
            }
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem is CharacterUi && newItem is CharacterUi -> oldItem == newItem
                else -> false
            }
        }
    }
}