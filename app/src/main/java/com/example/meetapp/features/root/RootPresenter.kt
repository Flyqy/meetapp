package com.example.meetapp.features.root

import com.example.meetapp.features.screens.Screens
import com.github.terrakok.cicerone.Router
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class RootPresenter(
    private val fragmentRouter: Router
) : MvpPresenter<RootView>() {

    init {
        fragmentRouter.navigateTo(Screens.SearchScreen())
//        fragmentRouter.navigateTo(Screens.OnboardingScreen(), true)
    }

    fun onNavigationItemClick(tabId: Int) {
//        when (tabId) {
//            R.id.near -> fragmentRouter.navigateTo(Screens.NearScreen(), true)
//            R.id.likes -> fragmentRouter.navigateTo(Screens.CouplesScreen(), true)
//            R.id.logo -> fragmentRouter.navigateTo(Screens.MainScreen(), true)
//            R.id.sms -> fragmentRouter.navigateTo(Screens.MeetSelectScreen(), true)
//            R.id.profile -> fragmentRouter.navigateTo(Screens.ProfileScreen(), true)
//        }
    }
}