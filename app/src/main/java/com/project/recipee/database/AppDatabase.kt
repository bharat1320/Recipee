package com.project.recipee.database

import android.content.Context
import androidx.room.*
import com.project.recipee.data.Dish
import com.project.recipee.util.Converters
import com.project.recipee.viewModel.dao.BookmarksDao

@Database(entities = [Dish :: class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(){

    abstract fun bookmarksDao() : BookmarksDao

    companion object{

        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getDatabaseInstance(context :Context) : AppDatabase {
            synchronized(this) {

                val tempInstance = INSTANCE
                if (tempInstance != null) {
                    return tempInstance
                }

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                return instance

            }
        }
    }
}