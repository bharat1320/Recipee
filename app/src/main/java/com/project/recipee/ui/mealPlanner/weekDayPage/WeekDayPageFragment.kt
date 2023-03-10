package com.project.recipee.ui.mealPlanner.weekDayPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.project.recipee.databinding.FragmentWeekDayPageBinding
import com.project.recipee.ui.mealPlanner.MealPlanningFragment
import com.project.recipee.viewModel.MainViewModel
import com.project.recipee.viewModel.RecipeViewModel

class WeekDayPageFragment(
    val position: Int,
    val day: String,
    val weekPlan: MutableLiveData<ArrayList<String>>
) : Fragment() {
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

        binding.weekDay.text = "Day : ${day.replace("    ", " ")}"
        binding.weekDayBreakfast.text = MealPlanningFragment.mealBrekfast
        binding.weekDayLunch.text = MealPlanningFragment.mealLunch
        binding.weekDayDinner.text = MealPlanningFragment.mealDinner

        setData()

        observer()
    }

    private fun observer() {
        weekPlan.observe(viewLifecycleOwner) { list ->
            list.forEach {
                if(it.contains(day)) {
                    binding.weekDayBreakfast.append((it.substringAfter(MealPlanningFragment.mealBrekfast)).substringBefore(MealPlanningFragment.mealLunch))
                    binding.weekDayLunch.append((it.substringAfter(MealPlanningFragment.mealLunch)).substringBefore(MealPlanningFragment.mealDinner))
                    binding.weekDayDinner.append(it.substringAfter(MealPlanningFragment.mealDinner))
                }
            }
        }
    }

    private fun setData() {

    }
}