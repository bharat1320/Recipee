package com.project.recipee.ui.home.adapters.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.project.recipee.data.Dish
import com.project.recipee.viewModel.repository.api.RecipeApis
import retrofit2.HttpException
import java.io.IOException

class HomeRvPagingSource constructor(private val api: RecipeApis,
                                     private val query :String,
                                     private val cuisine :String,
                                     private val sort :String
                                     ) : PagingSource<Int, Dish>() {
    companion object{
//        Count of items to load in single page, its called number as its the parameter name in API
        val number = 20
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Dish> {
        val page = params.key ?: 0
        return try{
            val response = api.getDishList(query,cuisine,sort,10,page)
            LoadResult.Page(
                response.results,
                prevKey = if(page == 0) null else page,
                nextKey = if(response.results.isEmpty()) null else page + number
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, Dish>): Int? {
        return null
    }
}