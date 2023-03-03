package com.project.recipee.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.recipee.viewModel.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: RecipeRepository
) :ViewModel() {

    var recipeResponse : MutableLiveData<Any> = MutableLiveData()

    fun getRecipe() {
        viewModelScope.launch(Dispatchers.IO) {
            recipeResponse.postValue(repository.getRecipeData("https://api.spoonacular.com/food/ingredients/search?query=banana&number=2&sort=calories&sortDirection=desc"))
        }
    }
}