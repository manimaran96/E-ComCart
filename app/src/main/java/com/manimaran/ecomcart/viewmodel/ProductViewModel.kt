package com.manimaran.ecomcart.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.manimaran.ecomcart.data.api.RxClient
import com.manimaran.ecomcart.databases.AppDatabase
import com.manimaran.ecomcart.databases.entities.Product
import com.manimaran.ecomcart.viewmodel.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProductViewModel(application: Application) : BaseViewModel(application) {

    private val productLiveData: MutableLiveData<List<Product>> = MutableLiveData()

    init {
        fetchProductsDetails()
    }

    fun getProducts(): LiveData<List<Product>> {
        return productLiveData
    }

    private fun fetchProductsDetails() {

        addToDisposable(RxClient.apiHost.getProductsData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                if (it != null) {
                    val data = mutableListOf<Product>()
                    val cartProductDao =
                        AppDatabase.getDatabase(getApplication())
                            .productDao()
                    for (prod in it.products) {
                        data.add(
                            Product(
                                prod.id,
                                prod.name,
                                prod.image,
                                prod.price,
                                prod.special,
                                cartProductDao.getProductCartCountById(prod.id)
                            )
                        )
                    }
                    productLiveData.value = data
                }
            })
    }

}