package com.manimaran.ecomcart.data.api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RxClient{
    val apiHost : RxApiServer by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.mocky.io/v3/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(RxApiServer::class.java)
    }
}