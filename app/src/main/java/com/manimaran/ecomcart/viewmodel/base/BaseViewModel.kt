package com.manimaran.ecomcart.viewmodel.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    private val disposables: CompositeDisposable by lazy { CompositeDisposable() }

    fun addToDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}