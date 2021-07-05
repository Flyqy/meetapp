package com.example.meetapp.stuff.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.Callback
import androidx.recyclerview.widget.DiffUtil.DiffResult
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.meetapp.stuff.put
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlin.reflect.KClass

open class EasyAdapter<T : Any>(
    protected val manager: AdapterDelegatesManager<List<T>>
) : RecyclerView.Adapter<ViewHolder>() {

    private var diffDisposable: Disposable? = null

    private var maxScheduledGeneration = 0

    var items: MutableList<T> = mutableListOf()
        private set

    protected open val diffItemCallback: DiffUtil.ItemCallback<T>? = null

    val viewTypes: IntArray
        get() = manager.delegateViewsTypes()

    constructor(vararg delegates: AdapterDelegate<List<T>>) : this(
        AdapterDelegatesManager<List<T>>().apply {
            delegates.forEach { delegate ->
                addDelegate(delegate)
            }
        }
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return manager.onCreateViewHolder(parent, viewType)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        manager.onBindViewHolder(items, position, holder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        manager.onBindViewHolder(items, position, holder, payloads)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        manager.onViewDetachedFromWindow(holder)
    }

    override fun onFailedToRecycleView(holder: ViewHolder): Boolean {
        return manager.onFailedToRecycleView(holder)
    }

    override fun getItemViewType(position: Int): Int {
        return manager.getItemViewType(items, position)
    }

    override fun onViewRecycled(holder: ViewHolder) {
        manager.onViewRecycled(holder)
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        manager.onViewAttachedToWindow(holder)
    }

    fun getViewTypesByDelegateClass(vararg delegateClasses: KClass<*>): IntArray = delegateClasses
        .map { delegateClass ->
            manager.findViewTypeByDelegate { delegate ->
                delegate::class == delegateClass
            }
        }.toIntArray()

    fun getDelegateForViewType(viewType: Int): AdapterDelegate<List<T>>? = manager.getDelegateForViewType(viewType)

    fun addTo(position: Int, item: T): Int {
        val pos = if (position <= items.size) {
            items.add(position, item)
            position
        } else {
            items.add(item)
            items.lastIndex
        }
        notifyItemInserted(pos)
        return pos
    }

    fun removeItem(item: T): Int {
        val iof = items.indexOf(item)
        if (iof != -1) {
            items.removeAt(iof)
            notifyItemRemoved(iof)
        }

        return iof
    }

    fun removeItemByIndex(index: Int) {
        if (index < 0 || index > itemCount) return
        items.removeAt(index)
        notifyItemRemoved(index)
    }

    fun updateItem(position: Int, item: T, payload: Any? = null) {
        if (position >= itemCount) return
        items[position] = item
        notifyItemChanged(position, payload)
    }

    fun getPositionByItem(item: T): Int {
        return items.indexOf(item)
    }

    fun putItems(items: List<T>) {
        if (this.items.isEmpty()) {
            this.items = items.toMutableList()
        } else {
            val toDelete = this.items.toMutableList()
            items.forEach {
                toDelete.remove(it)
                this.items.put(it)
            }

            this.items.removeAll(toDelete)
            notifyDataSetChanged()
        }
    }

    fun copy(): List<T> = this.items.toList()

    fun replaceItems(
        items: List<T>,
        withDiff: Boolean = false,
        async: Boolean = false,
        detectMoves: Boolean = false
    ) {
        if (this.items === items) {
            return
        }
        val itemCallback = diffItemCallback
        if (withDiff && itemCallback != null) {
            val oldItems = this.items
            if (async) {
                replaceItemsAsync(oldItems, items, detectMoves, itemCallback)
            } else {
                replaceItemsSync(oldItems, items, detectMoves, itemCallback)
            }
        } else {
            justReplaceItems(items)
        }
    }

    private fun justReplaceItems(newItems: List<T>) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }

    protected fun replaceItemsAsync(
        oldItems: List<T>,
        newItems: List<T>,
        detectMoves: Boolean,
        diffUtil: ItemCallback<T>
    ) {
        diffDisposable?.dispose()
        diffDisposable = Single
            .fromCallable { createDiff(oldItems, newItems, detectMoves, diffUtil) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { diffResult ->
                this.latchList(newItems, diffResult)
            }
    }

    protected fun replaceItemsSync(
        oldItems: List<T>,
        newItems: List<T>,
        detectMoves: Boolean,
        itemCallback: ItemCallback<T>
    ) {
        latchList(newItems, createDiff(oldItems, newItems, detectMoves, itemCallback))
    }

    private fun latchList(newItems: List<T>, diffResult: DiffResult) {
        items = newItems.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }

    companion object {
        fun <T : Any> createDiff(
            oldItems: List<T>,
            newItems: List<T>,
            detectMoves: Boolean,
            itemCallback: ItemCallback<T>
        ) = DiffUtil.calculateDiff(object : Callback() {

            override fun areItemsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ) = itemCallback.areItemsTheSame(
                oldItems[oldItemPosition],
                newItems[newItemPosition]
            )

            override fun getOldListSize() = oldItems.size

            override fun getNewListSize() = newItems.size

            override fun areContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ) = itemCallback.areContentsTheSame(
                oldItems[oldItemPosition],
                newItems[newItemPosition]
            )

            override fun getChangePayload(
                oldItemPosition: Int,
                newItemPosition: Int
            ) = itemCallback.getChangePayload(
                oldItems[oldItemPosition],
                newItems[newItemPosition]
            )
        }, detectMoves)
    }
}
