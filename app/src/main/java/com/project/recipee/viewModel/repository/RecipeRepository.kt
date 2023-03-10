package com.project.recipee.viewModel.repository

import android.util.Log
import com.project.recipee.BuildConfig.BASE_URL
import com.project.recipee.data.*
import com.project.recipee.network.URLS
import com.project.recipee.ui.MainActivity
import com.project.recipee.viewModel.repository.api.RecipeApis
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class RecipeRepository
@Inject constructor(
    val api : RecipeApis
) {
    suspend fun getRecipeData(url :String): String {
        return try{
            api.getRecipe(url)
        } catch(e :Exception) {
            e.toString()
        }
    }

    suspend fun getIngredientsList(id :Int): Ingredients? {
        return try{
            val value = api.getIngredientsList(id)
            value
        } catch(e :Exception) {
            Log.e(MainActivity.message_tag,e.toString())
            null
        }
    }

    suspend fun getRecipeInstructions(id :Int): String? {
        return try{
            val value = api.getRecipeInstructions(id).recipee
            value
        } catch(e :Exception) {
            Log.e(MainActivity.message_tag,e.toString())
            null
        }
    }

    suspend fun getNutritionalValue(id :Int): Nutrients? {
        return try{
            val value = api.getNutritionalValue(id)
            value
        } catch(e :Exception) {
            Log.e(MainActivity.message_tag,e.toString())
            null
        }
    }

    suspend fun getDishSearchList(query :String): PagingResponse<Dish>? {
        return try{
            val value = api.getDishList(query,"","",5,0)
            value
        } catch(e :Exception) {
            Log.e(MainActivity.message_tag,e.toString())
            null
        }
    }

}