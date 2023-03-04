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
import com.project.recipee.viewModel.repository.api.RecipeApis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: RecipeRepository
) :ViewModel() {

    fun getDishList(query :String = "", cuisine :String = "", sort :String = "") : Flow<PagingData<Dish>> = Pager(
        config = PagingConfig(10,enablePlaceholders = false)
    ){
        HomeRvPagingSource(repository.api,query,cuisine,sort)
    }.flow.cachedIn(viewModelScope)

}