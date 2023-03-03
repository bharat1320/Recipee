package com.project.recipee.viewModel.repository.api

import retrofit2.http.GET
import retrofit2.http.Url

interface RecipeApis {
    @GET
    suspend fun getNews(@Url url :String) : Map<String,Any>

    @GET
    suspend fun getRecipe(@Url url :String) : Any
}