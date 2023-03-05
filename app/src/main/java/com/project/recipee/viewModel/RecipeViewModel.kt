package com.project.recipee.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.project.recipee.data.Dish
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

    var recipeData : MutableLiveData<String> = MutableLiveData()

    fun getDishList(query :String = "", cuisine :String = "", sort :String = "") : Flow<PagingData<Dish>> = Pager(
        config = PagingConfig(HomeRvPagingSource.number,enablePlaceholders = false)
    ){
        HomeRvPagingSource(repository.api,query,cuisine,sort)
    }.flow.cachedIn(viewModelScope)

    fun getRecipeData(url :String) {
        CoroutineScope(Dispatchers.IO).launch {
            recipeData.postValue(repository.getRecipeData(url))
        }
    }

}