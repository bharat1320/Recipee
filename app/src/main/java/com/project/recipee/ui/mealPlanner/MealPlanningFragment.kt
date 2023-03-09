package com.project.recipee.ui.mealPlanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.project.recipee.R
import com.project.recipee.databinding.FragmentHomeBinding
import com.project.recipee.databinding.FragmentMealPlanningBinding
import com.project.recipee.ui.home.adapters.HomeRvPagingAdapter
import com.project.recipee.viewModel.MainViewModel
import com.project.recipee.viewModel.RecipeViewModel

class MealPlanningFragment : Fragment() {
    lateinit var binding : FragmentMealPlanningBinding
    lateinit var mainViewModel: MainViewModel
    lateinit var vm : RecipeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMealPlanningBinding.inflate(inflater, container, false)
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

    private fun adapters() {

    }

    private fun getData() {

    }

    private fun listener() {

    }
}