package com.project.recipee.ui.mealPlanner.weekDayPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.project.recipee.databinding.FragmentWeekDayPageBinding
import com.project.recipee.viewModel.MainViewModel
import com.project.recipee.viewModel.RecipeViewModel

class WeekDayPageFragment(val position: Int,val day: String) : Fragment() {
    lateinit var binding : FragmentWeekDayPageBinding
    lateinit var mainViewModel: MainViewModel
    lateinit var vm : RecipeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeekDayPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm = ViewModelProvider(requireActivity())[RecipeViewModel::class.java]
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        binding.weekDate.text = day
    }
}