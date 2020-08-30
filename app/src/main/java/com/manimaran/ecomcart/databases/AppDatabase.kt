package com.manimaran.ecomcart.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.manimaran.ecomcart.databases.dao.ProductDao
import com.manimaran.ecomcart.databases.entities.Product

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "product_db"
                    ).allowMainThreadQueries().build()
                }
                return INSTANCE as AppDatabase
            }
        }
    }

}