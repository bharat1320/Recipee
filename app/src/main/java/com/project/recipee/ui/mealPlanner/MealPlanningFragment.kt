package com.project.recipee.ui.mealPlanner

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.project.recipee.data.Dish
import com.project.recipee.databinding.FragmentMealPlanningBinding
import com.project.recipee.ui.MainActivity
import com.project.recipee.ui.mealPlanner.weekDayPage.WeekDayPageFragment
import com.project.recipee.util.CustomArrayAdapter
import com.project.recipee.util.SearchViewItemClicked
import com.project.recipee.viewModel.MainViewModel
import com.project.recipee.viewModel.RecipeViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MealPlanningFragment : Fragment(), SearchViewItemClicked {
    lateinit var binding : FragmentMealPlanningBinding
    lateinit var mainViewModel: MainViewModel
    lateinit var vm : RecipeViewModel
    lateinit var sharedPreference :SharedPreferences
    lateinit var customArrayAdapter: CustomArrayAdapter

    companion object{
        var days = arrayListOf<String>()

        val mealBrekfast = "Breakfast : "
        val mealLunch = "Lunch : "
        val mealDinner = "Dinner : "
    }

    var searchThreadAvailability = false
    private val timeDelay = 0.8 // in seconds


    var weekPlan : MutableLiveData<ArrayList<String>> = MutableLiveData()

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
        sharedPreference = requireActivity().getSharedPreferences(MainActivity.WEEK_PLAN_SHARED_PREFERENCE, Context.MODE_PRIVATE)

        updateWeekPlan()

        setTabsForWeek()

        adapters()

        listener()

        observer()

    }

    @SuppressLint("SimpleDateFormat")
    private fun setTabsForWeek() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd    EEE")

        for (i in 0..6) {
            val date = dateFormat.format(calendar.time)
            calendar.add(Calendar.DATE, 1)
            binding.mealPlannerTabLayout.addTab(binding.mealPlannerTabLayout.newTab().setText(date))
            days.add(date)
        }
    }

    private fun adapters() {
        binding.mealPlannerViewPager.adapter = (object : FragmentStateAdapter(this) {
            override fun getItemCount() = days.size
            override fun createFragment(position: Int): Fragment {
                return WeekDayPageFragment(position, days[position], weekPlan)
        }})

        TabLayoutMediator(binding.mealPlannerTabLayout, binding.mealPlannerViewPager) { tab, position ->
            tab.text = days[position]
        }.attach()

        customArrayAdapter = CustomArrayAdapter(requireContext(), arrayListOf(),this)
        binding.mealPlannerSearch.setAdapter(customArrayAdapter)
    }

    private fun listener() {
        binding.mealPlannerBack.setOnClickListener {
            mainViewModel.backPressed()
        }

        binding.mealPlannerSearch.doOnTextChanged { it, start, before, count ->
            if(it?.isBlank() == true) {
                binding.mealPlannerSearch.dismissDropDown()
            } else {
                if(!searchThreadAvailability) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        getData(it.toString())
                        searchThreadAvailability = false
                    }, (timeDelay * 1000).toLong())
                }
            }
        }
    }

    private fun observer() {
        vm.dishSearchList.observe(viewLifecycleOwner) {
            customArrayAdapter.updateData(it.data,it.query)
        }
    }

    fun getData(query: String) {
        vm.getDishSearchList(query)
    }

    override fun itemClicked(item: Dish) {
        binding.mealPlannerSearch.setText("")
    }

    fun updateWeekPlan() {
        if(sharedPreference.contains(MainActivity.WEEK_PLAN)) {
            days.clear()
            val set = sharedPreference.getStringSet(MainActivity.WEEK_PLAN, null)
            set?.forEach {
                days.add(it?:"")
            }
            weekPlan.postValue(days)
        }
    }
}