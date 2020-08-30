package com.manimaran.ecomcart.databases.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.manimaran.ecomcart.databases.entities.Product

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(product : Product)

    @Update
    fun update(product : Product)

    @Delete
    fun delete(product : Product)

    @get:Query("SELECT * FROM product_table ORDER BY name")
    val productList: LiveData<List<Product>>

    @Query("SELECT * FROM product_table WHERE id = :id")
    fun getProductById(id: String): Product

    @Query("SELECT count FROM product_table WHERE id = :id")
    fun getProductCartCountById(id: String): Int

}