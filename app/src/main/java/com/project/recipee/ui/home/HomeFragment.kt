package com.project.recipee.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.project.recipee.R
import com.project.recipee.database.AppDatabase
import com.project.recipee.databinding.FragmentHomeBinding
import com.project.recipee.ui.MainActivity
import com.project.recipee.viewModel.MainViewModel
import com.project.recipee.viewModel.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    lateinit var binding :FragmentHomeBinding
    lateinit var vm :RecipeViewModel
    lateinit var mainViewModel: MainViewModel
    lateinit var cuisineList: Array<String>

    private lateinit var appDb : AppDatabase

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
        vm.getRecipe()
    }

    fun adapters() {
        cuisineList = resources.getStringArray(R.array.cuisine_list)

    }

    fun observers() {
        vm.recipeResponse.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), "got data", Toast.LENGTH_SHORT).show()
        }
    }

    fun listener() {
        binding.homeBookmarks.setOnClickListener {

        }
    }

}