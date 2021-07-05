package com.example.meetapp.features.search.delegates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meetapp.R
import com.example.meetapp.stuff.recycler.EasyDelegate
import com.google.android.material.button.MaterialButton

class PageLoadingErrorDelegate :
    EasyDelegate<Any, PageLoadingErrorItem, PageLoadingErrorViewHolder>() {

    override fun isForViewType(item: Any): Boolean = item is PageLoadingErrorItem

    override fun onCreateViewHolder(parent: ViewGroup): PageLoadingErrorViewHolder =
        PageLoadingErrorViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_page_loading_error,
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        item: PageLoadingErrorItem,
        holder: PageLoadingErrorViewHolder,
        payloads: List<Any>
    ) {
        with(holder) {
            errorTextView.text = item.errorText
            retryButton.setOnClickListener { item.onRetryListener() }
        }
    }
}

class PageLoadingErrorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val errorTextView: TextView = view.findViewById(R.id.error_text)
    val retryButton: MaterialButton = view.findViewById(R.id.page_error_retry)
}

class PageLoadingErrorItem(
    val errorText: String,
    val onRetryListener: () -> Unit
)
