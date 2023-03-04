package com.project.recipee.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.recipee.R
import com.project.recipee.data.Dish
import com.project.recipee.database.AppDatabase
import com.project.recipee.databinding.FragmentHomeBinding
import com.project.recipee.ui.MainActivity
import com.project.recipee.ui.home.adapters.HomeRvItemClicked
import com.project.recipee.ui.home.adapters.HomeRvPagingAdapter
import com.project.recipee.viewModel.MainViewModel
import com.project.recipee.viewModel.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeFragment : Fragment(), HomeRvItemClicked {
    lateinit var binding :FragmentHomeBinding
    lateinit var vm :RecipeViewModel
    lateinit var mainViewModel: MainViewModel
    lateinit var adapter: HomeRvPagingAdapter

    private lateinit var appDb : AppDatabase

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

        vm = ViewModelProvider(this)[RecipeViewModel::class.java]
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        appDb = AppDatabase.getDatabaseInstance(activity as MainActivity)

        adapters()

        observers()

        getData()

        listener()

    }

    fun getData() {

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

        binding.homeNewsRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = HomeRvPagingAdapter(requireContext(),binding.homeNewsRv,this)
        binding.homeNewsRv.adapter = adapter
    }

    fun observers() {
        lifecycleScope.launchWhenStarted {
            vm.getDishList().collectLatest { data ->
                adapter.submitData(data)
            }
        }
    }

    fun listener() {
        binding.homeCuisineFilter.setOnItemClickListener { _, _, position, _ ->
            var item = cuisineList[position]
            item = if(item == "All") "" else item

        }

        binding.homeSortFilter.setOnItemClickListener { _, _, position, _ ->
            var item = sortCategoryList[position]
            item = if(item == "All") "" else item

        }

        binding.homeShopping.setOnClickListener {

        }
        binding.homeShoppingImage.setOnClickListener {

        }
    }

    override fun itemCLicked(item: Dish) {
//        Toast.makeText(requireContext(), "hiiii", Toast.LENGTH_SHORT).show()

    }

    override fun itemShareCLicked(item: Dish) {

    }

    override fun itemAddToCartCLicked(item: Dish) {

    }

    override fun itemRemoveFromCartCLicked(item: Dish) {

    }

}