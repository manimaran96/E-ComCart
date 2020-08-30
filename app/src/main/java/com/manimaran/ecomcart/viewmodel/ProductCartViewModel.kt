package com.manimaran.ecomcart.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.manimaran.ecomcart.databases.AppDatabase
import com.manimaran.ecomcart.databases.dao.ProductDao
import com.manimaran.ecomcart.databases.entities.Product
import com.manimaran.ecomcart.viewmodel.base.BaseViewModel

class ProductCartViewModel(application: Application) : BaseViewModel(application) {

    private val productDao: ProductDao = AppDatabase.getDatabase(application.applicationContext).productDao()
    val allProducts: LiveData<List<Product>>

    init {
        allProducts = productDao.productList
    }
}