package com.project.recipee.data

data class Dish(
    val id : Int,
    val title : String,
    val image : String,
    val nutrition : ArrayList<Nutrient> = arrayListOf()
)