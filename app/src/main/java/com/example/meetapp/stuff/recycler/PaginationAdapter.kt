package com.example.meetapp.stuff.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.meetapp.features.search.delegates.PageLoadingErrorDelegate
import com.example.meetapp.stuff.recycler.delegates.LoaderDelegate
import com.example.meetapp.stuff.recycler.delegates.PageLoadingDelegate
import kotlin.reflect.KClass

abstract class PaginationAdapter(
    private vararg val delegates: AdapterDelegate<List<Any>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected val items = mutableListOf<Any>()

    protected open fun getInitialDelegates() = mutableListOf(
        LoaderDelegate(),
        PageLoadingDelegate(),
        PageLoadingErrorDelegate()
    )

    protected val adapter: EasyAdapter<Any>
    protected open val diffItemCallback: DiffUtil.ItemCallback<Any>? = null
    protected open val innerDiffItemCallback = object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(
            oldItem: Any,
            newItem: Any
        ): Boolean = diffItemCallback?.areItemsTheSame(oldItem, newItem) ?: false

        override fun areContentsTheSame(
            oldItem: Any,
            newItem: Any
        ): Boolean = diffItemCallback?.areContentsTheSame(oldItem, newItem) ?: false

        override fun getChangePayload(oldItem: Any, newItem: Any): Any? =
            diffItemCallback?.getChangePayload(oldItem, newItem) ?: super.getChangePayload(oldItem, newItem)
    }

    val derivedViewTypes: IntArray

    init {
        adapter = initAdapter()

        derivedViewTypes = delegates.map { delegate -> delegate::class }
            .toTypedArray()
            .let { delegatesArray -> adapter.getViewTypesByDelegateClass(*delegatesArray) }

        adapter.registerAdapterDataObserver(Observer(this))
    }

    private fun initAdapter() =
        object : EasyAdapter<Any>() {

            init {
                getInitialDelegates().forEach { delegate ->
                    manager.addDelegate(delegate)
                }
                delegates.forEach { delegate ->
                    manager.addDelegate(delegate)
                }
            }

            override val diffItemCallback: DiffUtil.ItemCallback<Any>?
                get() = this@PaginationAdapter.innerDiffItemCallback
        }

    override fun getItemCount(): Int = adapter.itemCount

    override fun getItemViewType(position: Int): Int = adapter.getItemViewType(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        adapter.onCreateViewHolder(parent, viewType)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        adapter.onBindViewHolder(holder, position)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) =
        adapter.onBindViewHolder(holder, position, payloads)

    fun getViewTypesByDelegateClass(vararg delegateClasses: KClass<*>) =
        adapter.getViewTypesByDelegateClass(*delegateClasses)

    fun getDelegateForViewType(viewType: Int): AdapterDelegate<List<Any>>? = adapter.getDelegateForViewType(viewType)

    fun showStub(stubItem: Any) {
        items.clear()
        items.add(stubItem)
        updateItems()
    }

    fun showFashionStub(stubFashionItem: Any) {
        items.clear()
        items.add(stubFashionItem)
        updateItems()
    }

    fun hideStub() {
        items.clear()
        updateItems()
    }

    fun showLoader() {
        items.clear()
        //items.add(LoaderItem)
        updateItems()
    }

    fun hideLoader() {
        items.clear()
        updateItems()
    }

    fun showPageLoader() {
        //val firstItemIsStub = items.firstOrNull() is StubItem
        //val firstItemIsStubFashion = items.firstOrNull() is StubFashionItem
        //val lastItemIsPageLoading = items.lastOrNull() is PageLoadingItem
        //val lastItemIsPageLoadingError = items.lastOrNull() is PageLoadingErrorItem
        //if (!firstItemIsStub && !lastItemIsPageLoading && !lastItemIsPageLoadingError && !firstItemIsStubFashion) {
        //    items.add(PageLoadingItem)
        //    updateItems(withDiff = true)
        //}
    }

    //fun hidePageLoader() {
    //    items.remove(PageLoadingItem)
    //    updateItems(withDiff = true)
    //}

    //fun showPageLoadingError(pageLoadingErrorItem: PageLoadingErrorItem) {
    //    val firstItemIsStub = items.firstOrNull() is StubItem
    //    val firstItemIsStubFashion = items.firstOrNull() is StubFashionItem
    //    val lastItemIsPageLoadingError = items.lastOrNull() is PageLoadingErrorItem
    //    val lastItemIsPageLoading = items.lastOrNull() is PageLoadingItem
    //    if (!firstItemIsStub && !lastItemIsPageLoadingError && !lastItemIsPageLoading && !firstItemIsStubFashion) {
    //        items.add(pageLoadingErrorItem)
    //        updateItems()
    //    }
    //}

    //fun hidePageLoadingError() {
    //    if (items.lastOrNull() is PageLoadingErrorItem) {
    //        items.removeAt(items.lastIndex)
    //        updateItems()
    //    }
    //}
//
    //fun addHeader(header: View) {
    //    headers.add(HeaderItem(header))
    //    updateItems()
    //}
//
    //fun removeHeader(header: View) {
    //    headers.remove(HeaderItem(header))
    //    updateItems()
    //}
//
    //fun addFooter(footer: View) {
    //    footers.add(FooterItem(footer))
    //    updateItems()
    //}
//
    //fun removeFooter(footer: View) {
    //    footers.remove(FooterItem(footer))
    //    updateItems()
    //}

    fun replaceItems(
        items: List<Any>,
        withDiff: Boolean = false,
        async: Boolean = false,
        detectMoves: Boolean = false
    ) {
        this.items.clear()
        this.items.addAll(items)
        updateItems(withDiff, async, detectMoves)
    }

    //fun setInListBanner(banner: View) {
    //    bannerView = OtherView(banner)
    //    if (items.count() < CONDITION_PROMOTED_ANALYTICS) {
    //        items.add(items.count(), bannerView)
    //    } else {
    //        items.add(CONDITION_PROMOTED_ANALYTICS, bannerView)
    //    }
    //    updateItems()
    //}
//
    //fun removeBanner() {
    //    if (this::bannerView.isInitialized && items.contains(bannerView)) {
    //        items.remove(bannerView)
    //        updateItems(withDiff = true)
    //    }
    //}

    protected open fun updateItems(
        withDiff: Boolean = false,
        async: Boolean = false,
        detectMoves: Boolean = false
    ) {
        val list = combineItems()
        adapter.replaceItems(list, withDiff, async, detectMoves)
    }

    protected fun combineItems() = mutableListOf<Any>().apply {
        //if (items.firstOrNull() is StubItem ||
        //    items.firstOrNull() is StubFashionItem ||
        //    items.firstOrNull() is LoaderItem
        //) {
            addAll(items)
        //} else {
        //    addAll(headers)
        //    addAll(items)
        //    addAll(footers)
        //}
    }

    private class Observer(private val adapter: PaginationAdapter) : RecyclerView.AdapterDataObserver() {

        override fun onChanged() {
            adapter.notifyDataSetChanged()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            adapter.notifyItemRangeRemoved(positionStart, itemCount)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            adapter.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            adapter.notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            adapter.notifyItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(positionStart, itemCount, payload)
        }
    }

    companion object {
        const val CONDITION_PROMOTED_ANALYTICS = 5
    }
}
