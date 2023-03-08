package com.project.recipee.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "bookmarks")
data class LocalDish (
    @PrimaryKey var id: Int,
    @ColumnInfo(name = "title")var title : String,
    @ColumnInfo(name = "image")var image : String,
    @ColumnInfo(name = "nutrition") var nutrition : ArrayList<String>? = null,
    @SerializedName("ingredientList" ) var ingredientList : ArrayList<String>? = null,
    @SerializedName("recipee" ) var recipee : String? = null
)