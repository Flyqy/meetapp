package com.example.meetapp.base.presenter

import androidx.annotation.CallSuper
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import moxy.MvpPresenter
import moxy.MvpView
import io.reactivex.schedulers.Schedulers

abstract class BasePresenter<V : MvpView> : MvpPresenter<V>() {

    private val disposes = CompositeDisposable()

    protected fun <T> Single<T>.subscribeAsync(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit = {},
        onSubscribe: (Disposable) -> Unit = {},
        subscribeOn: Scheduler = Schedulers.io(),
        observeOn: Scheduler = AndroidSchedulers.mainThread(),
        unsubscribeOn: Scheduler = Schedulers.io()
    ) = subscribeOn(subscribeOn)
        .observeOn(observeOn)
        .unsubscribeOn(unsubscribeOn)
        .doOnSubscribe(onSubscribe)
        .subscribe(onSuccess, onError)
        .bind()

    private fun Disposable.bind() = disposes.add(this)

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        disposes.dispose()
    }
}