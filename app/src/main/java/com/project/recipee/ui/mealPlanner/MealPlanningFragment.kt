package com.project.recipee.ui.mealPlanner

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.project.recipee.databinding.FragmentMealPlanningBinding
import com.project.recipee.ui.mealPlanner.weekDayPage.WeekDayPageFragment
import com.project.recipee.viewModel.MainViewModel
import com.project.recipee.viewModel.RecipeViewModel
import java.text.SimpleDateFormat
import java.util.*

class MealPlanningFragment : Fragment() {
    lateinit var binding : FragmentMealPlanningBinding
    lateinit var mainViewModel: MainViewModel
    lateinit var vm : RecipeViewModel

    companion object{
        private var days = arrayListOf<String>()
    }

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

        setTabsForWeek()

        adapters()

        getData()

        listener()

    }

    @SuppressLint("SimpleDateFormat")
    private fun setTabsForWeek() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd    EEE")

        for (i in 0..6) {
            calendar.add(Calendar.DATE, 1)
            val date = dateFormat.format(calendar.time)
            days.add(date)
        }
    }

    private fun adapters() {
        binding.mealPlannerViewPager.adapter = (object : FragmentStateAdapter(this) {
            override fun getItemCount() = days.size
            override fun createFragment(position: Int): Fragment {
                return WeekDayPageFragment(position, days[position])
        }})

        TabLayoutMediator(binding.mealPlannerTabLayout, binding.mealPlannerViewPager) { tab, position ->
            tab.text = days[position]
        }.attach()
    }

    private fun getData() {

    }

    private fun listener() {
        binding.mealPlannerBack.setOnClickListener {
            mainViewModel.backPressed()
        }
    }
}