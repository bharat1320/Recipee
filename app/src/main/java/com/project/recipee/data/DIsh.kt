package com.project.recipee.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "bookmarks")
data class Dish(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title")val title : String,
    @ColumnInfo(name = "image")val image : String,
    @ColumnInfo(name = "nutrition") val nutrition : Nutrition? = null
)

data class Nutrition (
    @SerializedName("nutrients" ) var nutrients : ArrayList<Nutrient> = arrayListOf()
)