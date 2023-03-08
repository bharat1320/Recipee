package com.project.recipee.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.project.recipee.data.LocalDish
import com.project.recipee.database.AppDatabase
import com.project.recipee.databinding.FragmentFavouritesBinding
import com.project.recipee.databinding.RvDishItemBinding
import com.project.recipee.ui.MainActivity
import com.project.recipee.ui.recipeDetail.RecipeDetailFragment
import com.project.recipee.viewModel.MainViewModel
import com.project.recipee.viewModel.RecipeViewModel


class FavouritesFragment : Fragment() {
    lateinit var binding: FragmentFavouritesBinding
    lateinit var mainViewModel: MainViewModel
    lateinit var vm: RecipeViewModel
    private lateinit var appDb: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm = ViewModelProvider(requireActivity())[RecipeViewModel::class.java]
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        appDb = AppDatabase.getDatabaseInstance(activity as MainActivity)

        getData()

        listeners()

        observer()

    }

    fun getData() {
        vm.getDishFromBookmark(appDb)
    }

    fun listeners() {
        binding.favouriteBack.setOnClickListener {
            mainViewModel.backPressed()
        }
    }

    fun observer() {
        vm.dishFromBookmarks.observe(viewLifecycleOwner) {
            binding.favouriteRecipeChipGroup.removeAllViews()
//            addViewsToGridLayout(it)
            it.forEach {
                addRecipesInView(it)
            }
        }
    }

    fun addRecipesInView(item: LocalDish) {
        val layout = RvDishItemBinding.inflate(LayoutInflater.from(requireContext()))
        Glide.with(requireContext())
            .load(item.image)
            .into(layout.homeRvItemImage)
        layout.homeRvItemImage.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(RecipeDetailFragment.bundle_dishId,item.id)
            bundle.putString(RecipeDetailFragment.bundle_dishName,item.title)
            bundle.putString(RecipeDetailFragment.bundle_dishImage,item.image)
            bundle.putString(RecipeDetailFragment.bundle_recipee,item.recipee)
            bundle.putStringArrayList(RecipeDetailFragment.bundle_ingredients,item.ingredientList)
            bundle.putStringArrayList(RecipeDetailFragment.bundle_nutrition,item.nutrition)
            mainViewModel.callFragment(RecipeDetailFragment(),bundle)
        }
        layout.homeRvItemTitle.setOnClickListener {

        }
        layout.homeRvItemTitle.text = item.title
        layout.homeRvItemChip.chipLayout.visibility = View.GONE
        binding.favouriteRecipeChipGroup.addView(layout.root)
    }

//    private fun addViewsToGridLayout(it: List<LocalDish>) {
//        it.forEachIndexed { i, value ->
//            val view = RvDishItemBinding.inflate(LayoutInflater.from(requireContext()))
//            val params = GridLayout.LayoutParams()
//            params.columnSpec = GridLayout.spec(if (i % 2 == 0) 0 else 1)
//            params.rowSpec = GridLayout.spec(binding.favouriteRecipeChipGroup.rowCount)
//            params.setMargins(16, 16, 16, 16)
//            binding.favouriteRecipeChipGroup.addView(view.root, params)
//        }
//    }
}

