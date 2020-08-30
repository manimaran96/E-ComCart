package com.manimaran.ecomcart.databases.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_table")
class Product(
    @PrimaryKey val id: String,
    var name: String,
    var image: String,
    var price: String,
    @ColumnInfo(name = "special_price") var specialPrice: String,
    var count: Int = 0
)