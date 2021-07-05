package com.example.meetapp.features.search

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meetapp.R
import com.example.meetapp.base.fragment.BaseFragment
import com.example.meetapp.features.search.adapter.SearchListAdapter
import com.example.meetapp.stuff.fragment.hideSoftKeyboard
import com.example.meetapp.stuff.fragment.onTouchStartOrFirstMove
import com.example.meetapp.stuff.recycler.PagingListener
import com.example.meetapp.stuff.recycler.decoration.CustomDividerItemDecoration
import com.google.android.material.button.MaterialButton
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.koin.android.ext.android.inject

class SearchFragment : BaseFragment(), SearchView {

    @InjectPresenter
    lateinit var presenter: SearchPresenter

    private var characterAdapter: SearchListAdapter? = null

    private val scrollListener = PagingListener(SearchPresenter.PAGE_SIZE) {
        presenter.onEndReached()
    }

    @ProvidePresenter
    fun providePresenter(): SearchPresenter {
        val injectPresenter by inject<SearchPresenter>()
        return injectPresenter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        characterAdapter = SearchListAdapter()
        view.findViewById<RecyclerView>(R.id.charactersView).apply {
            adapter = characterAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(scrollListener)
            characterAdapter?.let {
                addItemDecoration(
                    CustomDividerItemDecoration(
                        requireContext(),
                        it.derivedViewTypes,
                        R.dimen.spacing_8dp
                    )
                )
            }
            onTouchStartOrFirstMove { requireView().hideSoftKeyboard() }
        }
        view.findViewById<androidx.appcompat.widget.SearchView>(
            R.id.searchView
        ).setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                presenter.onQueryChange(newText ?: String())
                return true
            }

        })
    }

    override fun updateItems(data: List<CharacterUi>) {
        requireView().findViewById<ConstraintLayout>(R.id.someThingWrong).visibility = View.GONE
        requireView().findViewById<ConstraintLayout>(R.id.networkError).visibility = View.GONE
        requireView().findViewById<FrameLayout>(R.id.search).visibility = View.VISIBLE
        requireView().findViewById<RecyclerView>(R.id.charactersView).visibility = View.VISIBLE
        characterAdapter?.replaceItems(data)
    }

    override fun showEmpty() = Unit

    override fun showLoader() {
        characterAdapter?.showLoader()
    }

    override fun showPageProgress() {
        characterAdapter?.showPageLoader()
    }

    override fun showError() {
        val cm =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (cm.activeNetworkInfo?.isConnected == true) {
            requireView().findViewById<ConstraintLayout>(R.id.networkError).visibility = View.GONE
            requireView().findViewById<ConstraintLayout>(R.id.someThingWrong).visibility =
                View.VISIBLE
            requireView().findViewById<FrameLayout>(R.id.search).visibility = View.GONE
            requireView().findViewById<RecyclerView>(R.id.charactersView).visibility = View.GONE
            requireView().findViewById<MaterialButton>(R.id.someThingWrongRetry).setOnClickListener {
                presenter.onRetryClick()
            }
        } else {
            requireView().findViewById<ConstraintLayout>(R.id.someThingWrong).visibility = View.GONE
            requireView().findViewById<ConstraintLayout>(R.id.networkError).visibility =
                View.VISIBLE
            requireView().findViewById<FrameLayout>(R.id.search).visibility = View.GONE
            requireView().findViewById<RecyclerView>(R.id.charactersView).visibility = View.GONE
            requireView().findViewById<MaterialButton>(R.id.networkRetry).setOnClickListener {
                presenter.onRetryClick()
            }
        }
    }

    companion object {
        const val MAIN_SCREEN = "MEET_SELECT_SCREEN"

        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }
}