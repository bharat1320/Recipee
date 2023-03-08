package com.project.recipee.data

import com.google.gson.annotations.SerializedName

data class Dish(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title : String,
    @SerializedName("image") val image : String,
    @SerializedName("nutrition") val nutrition : Nutrition? = null
)

data class Nutrition (
    @SerializedName("nutrients" ) var nutrients : ArrayList<Nutrient> = arrayListOf()
)