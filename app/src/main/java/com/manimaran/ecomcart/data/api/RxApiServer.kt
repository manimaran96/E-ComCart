package com.manimaran.ecomcart.data.api

import com.manimaran.ecomcart.data.model.ProductData
import io.reactivex.Observable
import retrofit2.http.GET

interface RxApiServer {
    @GET("6d26f9e9-0012-4545-84b0-0e27d0c1228d")
    fun getProductsData(): Observable<ProductData>
}