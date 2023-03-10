package com.project.recipee.data

import retrofit2.http.Query

data class SearchViewEntity(
    val id :Int,
    val name :String,
    val image :String
)

data class SearchEntity(
    val query :String,
    val data :ArrayList<Dish>
)
