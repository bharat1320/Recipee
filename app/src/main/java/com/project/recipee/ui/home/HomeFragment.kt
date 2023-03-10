package com.project.recipee.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.project.recipee.R
import com.project.recipee.data.Dish
import com.project.recipee.databinding.FragmentHomeBinding
import com.project.recipee.ui.cart.CartFragment
import com.project.recipee.ui.favourites.FavouritesFragment
import com.project.recipee.ui.home.adapters.HomeRvItemClicked
import com.project.recipee.ui.home.adapters.HomeRvPagingAdapter
import com.project.recipee.ui.home.adapters.LoaderAdapter
import com.project.recipee.ui.mealPlanner.MealPlanningFragment
import com.project.recipee.ui.recipeDetail.RecipeDetailFragment
import com.project.recipee.ui.register.RegisterFragment
import com.project.recipee.viewModel.MainViewModel
import com.project.recipee.viewModel.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), HomeRvItemClicked {
    lateinit var binding :FragmentHomeBinding
    lateinit var mainViewModel: MainViewModel
    lateinit var vm :RecipeViewModel
    lateinit var adapter: HomeRvPagingAdapter

    var lastSelectedQuery = "i"
    var lastSelectedCuisine = ""
    var lastSelectedSort = ""

//    This is for search optimization
    var searchThreadBusy = false
    private val timeDelay = 0.8 // in seconds

    companion object{
        lateinit var cuisineList: Array<String>
        lateinit var sortCategoryList: Array<String>
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProvider(requireActivity())[RecipeViewModel::class.java]
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        adapters()

        getData()

        listener()

    }

    fun getData() {
        adapter.submitData(lifecycle, PagingData.empty())
        adapter.refresh()
        lifecycleScope.launch(Dispatchers.IO) {
            vm.getDishList(lastSelectedQuery, lastSelectedCuisine, lastSelectedSort).collectLatest { data ->
                adapter.submitData(data)
            }
        }
    }

    fun adapters() {
        cuisineList = resources.getStringArray(R.array.cuisine_list)
        sortCategoryList = resources.getStringArray(R.array.sort_list)

        binding.homeCuisineFilter.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                cuisineList
            )
        )

        binding.homeSortFilter.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                sortCategoryList
            )
        )

        binding.homeNewsRv.layoutManager = GridLayoutManager(requireContext(),2)
//        binding.homeNewsRv.layoutManager = object : GridLayoutManager(requireContext(),2) {
//            override fun canScrollVertically(): Boolean {
//                return false
//            }
//        }
        adapter = HomeRvPagingAdapter(requireContext(),binding.homeNewsRv,this)
        binding.homeNewsRv.adapter = adapter.withLoadStateHeaderAndFooter(
            LoaderAdapter(),
            LoaderAdapter()
        )
    }

    fun listener() {

        binding.homeCalendar.setOnClickListener {
            mainViewModel.callFragment(MealPlanningFragment(),Bundle())
        }

        binding.homeAccount.setOnClickListener {
            mainViewModel.callFragment(RegisterFragment(),Bundle())
        }

        binding.homeCuisineFilter.setOnItemClickListener { _, _, position, _ ->
            var item = cuisineList[position]
            lastSelectedCuisine = if(item == resources.getString(R.string.all_cuisine)) "" else item
            getData()
        }

        binding.homeSortFilter.setOnItemClickListener { _, _, position, _ ->
            var item = sortCategoryList[position]
            lastSelectedSort = if(item == resources.getString(R.string.all_sort)) "" else item
            getData()
        }

        binding.homeShoppingCart.setOnClickListener {
            mainViewModel.callFragment(CartFragment(),Bundle())
        }

        binding.homeShoppingBookmark.setOnClickListener {
            mainViewModel.callFragment(FavouritesFragment(),Bundle())
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                checkSearchThreadAvailability(if(query == "") "i" else query)
                return false
            }
            override fun onQueryTextChange(query: String?): Boolean {
                checkSearchThreadAvailability(if(query == "") "i" else query)
                return false
            }
        })
    }

    fun checkSearchThreadAvailability(query : String?) {
        if(!searchThreadBusy) {
            Handler(Looper.getMainLooper()).postDelayed({
                lastSelectedQuery = query ?: lastSelectedQuery
                getData()
            }, (timeDelay * 1000).toLong())
        }
    }

    override fun itemCLicked(item: Dish) {
        val bundle = Bundle()
        bundle.putInt(RecipeDetailFragment.bundle_dishId,item.id)
        bundle.putString(RecipeDetailFragment.bundle_dishName,item.title)
        bundle.putString(RecipeDetailFragment.bundle_dishImage,item.image)
        mainViewModel.callFragment(RecipeDetailFragment(),bundle)
    }

}