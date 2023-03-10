package com.project.recipee.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.project.recipee.data.*
import com.project.recipee.database.AppDatabase
import com.project.recipee.ui.home.adapters.pagingSource.HomeRvPagingSource
import com.project.recipee.viewModel.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RecipeViewModel @Inject constructor(
    val repository: RecipeRepository
) :ViewModel() {

    fun getDishList(query :String = "", cuisine :String = "", sort :String = "") : Flow<PagingData<Dish>> = Pager(
        config = PagingConfig(HomeRvPagingSource.number,enablePlaceholders = false)
    ){
        HomeRvPagingSource(repository.api,query,cuisine,sort)
    }.flow.cachedIn(viewModelScope)

//    This is for Testing
    var recipeData : MutableLiveData<String> = MutableLiveData()
    fun getRecipeData(url :String) {
        CoroutineScope(Dispatchers.IO).launch {
            recipeData.postValue(repository.getRecipeData(url))
        }
    }

    var ingredientList : MutableLiveData<ArrayList<Ingredient>> = MutableLiveData()
    fun getIngredientsList(id :Int) {
        CoroutineScope(Dispatchers.IO).launch {
            ingredientList.postValue(repository.getIngredientsList(id)?.ingredients?: arrayListOf())
        }
    }

    var recipeInstructions : MutableLiveData<String> = MutableLiveData()
    fun getRecipeInstructions(id :Int) {
        CoroutineScope(Dispatchers.IO).launch {
            recipeInstructions.postValue(repository.getRecipeInstructions(id))
        }
    }

    var nutritionalValue : MutableLiveData<Nutrients> = MutableLiveData()
    fun getNutritionalValue(id :Int) {
        CoroutineScope(Dispatchers.IO).launch {
            nutritionalValue.postValue(repository.getNutritionalValue(id))
        }
    }

    fun addToBookmark(appDb : AppDatabase,data : LocalDish) {
        CoroutineScope(Dispatchers.IO).launch {
            appDb.bookmarksDao().addToBookmarks(data)
        }
    }

    fun removeFromBookmark(appDb : AppDatabase,data : LocalDish) {
        CoroutineScope(Dispatchers.IO).launch {
            appDb.bookmarksDao().deleteFromBookmarks(data)
        }
    }

    var dishFromBookmarks : MutableLiveData<List<LocalDish>> = MutableLiveData()
    fun getDishFromBookmark(appDb : AppDatabase) {
        CoroutineScope(Dispatchers.IO).launch {
            val data = appDb.bookmarksDao().getAllBookmarks()
            dishFromBookmarks.postValue(data)
        }
    }

    var refreshBookmarkPage : MutableLiveData<Boolean> = MutableLiveData()

    var dishSearchList : MutableLiveData<SearchEntity> = MutableLiveData()
    fun getDishSearchList(query : String) {
        CoroutineScope(Dispatchers.IO).launch {
            dishSearchList.postValue(SearchEntity(query,repository.getDishSearchList(query)?.results ?: arrayListOf()))
        }
    }

}