package com.project.recipee.viewModel.repository.api

import com.project.recipee.data.*
import com.project.recipee.network.URLS
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url
import java.net.URL

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

    @GET("{id}"+URLS.getIngredientList)
    suspend fun getIngredientsList(@Path("id") id :Int) : Ingredients

    @GET("{id}"+URLS.getRecipeInstructions)
    suspend fun getRecipeInstructions(@Path("id") id :Int) : Recipee

    @GET("{id}"+URLS.getNutritionalValue)
    suspend fun getNutritionalValue(@Path("id") id :Int) : Nutrients
}