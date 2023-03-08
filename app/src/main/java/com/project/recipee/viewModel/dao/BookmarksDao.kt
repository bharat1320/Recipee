package com.project.recipee.viewModel.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.recipee.data.LocalDish

@Dao
interface BookmarksDao {

    @Query("SELECT * FROM bookmarks")
    suspend fun getAllBookmarks() : List<LocalDish>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToBookmarks(dish: LocalDish)

    @Delete
    suspend fun deleteFromBookmarks(dish: LocalDish)

    // Here it returns number of rows deleted count
    @Query("DELETE FROM bookmarks")
    suspend fun deleteAllBookmarks() : Int

//     Here ":" is used before next id so that it takes the input id and not from table
//    @Query("SELECT * FROM bookmarks WHERE id LIKE :id LIMIT 1")
//    suspend fun getBookmarkOfId(id :Int) : List<Bookmark>
}