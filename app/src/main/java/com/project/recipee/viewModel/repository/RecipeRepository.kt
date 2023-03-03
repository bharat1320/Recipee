package com.project.recipee.viewModel.repository

import com.project.recipee.network.URLS
import com.project.recipee.viewModel.repository.api.RecipeApis
import dagger.hilt.android.scopes.ActivityRetainedScoped
import java.util.*
import javax.inject.Inject

@ActivityRetainedScoped
class RecipeRepository
@Inject constructor(
    private val api : RecipeApis
) {

    suspend fun getRecipeData(url :String): Any {
        return try{
            api.getRecipe(url)
        } catch(e :Exception) {

        }
    }

}