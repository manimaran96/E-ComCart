package com.manimaran.ecomcart.data.model

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val image: String,
    val quantity: Int,
    val price: String,
    val special: String
)