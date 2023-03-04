package com.project.recipee.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class Dish(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title")val title : String,
    @ColumnInfo(name = "image")val image : String,
    @ColumnInfo(name = "nutrition") val nutrition : ArrayList<Nutrient> = arrayListOf()
)