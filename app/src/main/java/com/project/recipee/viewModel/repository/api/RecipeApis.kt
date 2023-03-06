package com.project.recipee.viewModel.repository.api

import com.project.recipee.data.*
import com.project.recipee.network.URLS
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface RecipeApis {

    @GET
    suspend fun getRecipe(@Url url :String) : String

    @GET(URLS.complexSearch)
    suspend fun getDishList(
        @Query("query") query :String,
        @Query("cuisine") cuisine :String,
        @Query("sort") sort :String,
        @Query("number") number :Int,
        @Query("offset") page :Int
    ) : PagingResponse<Dish>

    @GET
    suspend fun getIngredientsList(@Url url :String) : Ingredients

    @GET
    suspend fun getRecipeInstructions(@Url url :String) : Recipee
}