package com.example.meetapp.features.root

import android.os.Bundle
import com.example.meetapp.R
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.koin.android.ext.android.inject

class RootActivity : MvpAppCompatActivity(), RootView {

    private val navigator = AppNavigator(this, R.id.container)

    private val navigatorHolder: NavigatorHolder by inject()

    @InjectPresenter
    lateinit var presenter: RootPresenter

    @ProvidePresenter
    fun providePresenter(): RootPresenter {
        val injectPresenter by inject<RootPresenter>()
        return injectPresenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }
}