package com.project.recipee.ui.mealPlanner

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
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
    var lastSelectedMealTime = "Breakfast"
    val lastSelectedDate get() = dates[binding.mealPlannerTabLayout.selectedTabPosition]

    companion object{
        var dates = arrayListOf<String>()
        var plans = arrayListOf<String>()

        val mealBrekfast = " Breakfast : "
        val mealLunch = " Lunch : "
        val mealDinner = " Dinner : "

        val mealTimes = arrayListOf("Breakfast","Lunch","Dinner")
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

        setData()

        adapters()

        listener()

        observer()

        updateWeekPlan()

    }

    private fun setData() {
        binding.mealPlannerMealSelector.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            mealTimes
        )

//        Set all the plans available
        plans.clear()
        val set = sharedPreference.getStringSet(MainActivity.WEEK_PLAN, null)
        set?.forEach {
            plans.add(it?:"")
        }
    }

    private fun adapters() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd EEE")

        dates.clear()
        for (i in 0..6) {
            val date = dateFormat.format(calendar.time)
            calendar.add(Calendar.DATE, 1)
            binding.mealPlannerTabLayout.addTab(binding.mealPlannerTabLayout.newTab().setText(date))
            dates.add(date)
        }

        binding.mealPlannerViewPager.adapter = (object : FragmentStateAdapter(this) {
            override fun getItemCount() = dates.size
            override fun createFragment(position: Int): Fragment {
                return WeekDayPageFragment(position, dates[position], weekPlan)
        }})

        TabLayoutMediator(binding.mealPlannerTabLayout, binding.mealPlannerViewPager) { tab, position ->
            tab.text = dates[position].replace(" ","    ")
        }.attach()

        customArrayAdapter = CustomArrayAdapter(requireContext(), arrayListOf(),this)
        binding.mealPlannerSearch.setAdapter(customArrayAdapter)
    }

    private fun listener() {
        binding.mealPlannerBack.setOnClickListener {
            mainViewModel.backPressed()
        }

        binding.mealPlannerMealSelector.onItemSelectedListener = (object : OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                lastSelectedMealTime = mealTimes[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        })

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
        val meal = item.title
        binding.mealPlannerSearch.setText("")
        var dayWasNotCreated = true
        plans.forEachIndexed { index, it ->
            if(it.contains(lastSelectedDate)) {
                dayWasNotCreated = false
                plans[index] = addMealToMealTime(meal,it,lastSelectedMealTime)
            }
        }
        if(dayWasNotCreated) {
            plans.add(
                addMealToMealTime(
                    meal,
                    "$lastSelectedDate,$mealBrekfast$mealLunch$mealDinner",
                    lastSelectedMealTime)
            )
        }
        plans
        updateWeekPlan()
    }

    fun addMealToMealTime(meal: String,plan :String, lastSelectedMealTime: String) : String{
        return if(mealBrekfast.contains(lastSelectedMealTime)) {
            plan.substringBefore(mealBrekfast) + mealBrekfast + meal + mealLunch + plan.substringAfter(mealLunch)
        } else if(mealLunch.contains(lastSelectedMealTime)) {
            plan.substringBefore(mealLunch) + mealLunch + meal + mealDinner + plan.substringAfter(mealDinner)
        } else {
            plan.substringBefore(mealDinner) + mealDinner + meal
        }
    }

    fun updateWeekPlan() {
        if(sharedPreference.contains(MainActivity.WEEK_PLAN)) {
            weekPlan.postValue(plans)
        } else {
            weekPlan.postValue(plans)
        }
        sharedPreference.edit().putStringSet(MainActivity.WEEK_PLAN, plans.toSet()).apply()
    }
}