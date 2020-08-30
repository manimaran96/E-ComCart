package com.manimaran.ecomcart.data.api

import com.manimaran.ecomcart.data.model.ProductData
import io.reactivex.Observable
import retrofit2.http.GET

interface RxApiServer {
    @GET("files/dummy_products.json")
    fun getProductsData(): Observable<ProductData>
}