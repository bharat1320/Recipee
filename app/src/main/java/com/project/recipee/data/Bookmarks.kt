package com.project.recipee.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class Bookmark(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title")val title : String,
    @ColumnInfo(name = "image")val image : String,
    @ColumnInfo(name = "urlToImage")val nutrition : ArrayList<Nutrient>
)

fun Bookmark.dishToBookmark(item :Dish) : Bookmark{
    return Bookmark(item.id,item.title,item.image,item.nutrition)
}
