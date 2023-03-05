package com.project.recipee.viewModel.repository

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

}