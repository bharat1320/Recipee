package com.project.recipee.ui.register

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.project.recipee.R
import com.project.recipee.databinding.FragmentRegisterBinding
import com.project.recipee.ui.MainActivity
import com.project.recipee.viewModel.MainViewModel

class RegisterFragment : Fragment() {
    lateinit var binding :FragmentRegisterBinding
    lateinit var mainViewModel: MainViewModel
    lateinit var sharedPreferences: SharedPreferences
    val sharedPreferencesEditor by lazy {
        sharedPreferences.edit()
    }
    var cuisineList = ArrayList<String>()
    var lastSelectedCuisine = ""

    val name get() = binding.registerName.itemInput.text.toString()
    val age get() = binding.registerAge.itemInput.text.toString()
    val weight get() = binding.registerWeight.itemInput.text.toString()
    val height get() = binding.registerHeight.itemInput.text.toString()
    val dietaryRestrictions get() = binding.registerDietaryRestrictions.itemInput.text.toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater,container,false)

        binding.registerName.itemTitle.text = "Name :"
        binding.registerAge.itemTitle.text = "Age :"
        binding.registerWeight.itemTitle.text = "Weight :"
        binding.registerHeight.itemTitle.text = "Height :"
        binding.registerDietaryRestrictions.itemTitle.text = "Dietary Restrictions :"

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        sharedPreferences =  requireActivity().getSharedPreferences(MainActivity.ACCOUNT_SHARED_PREFERENCE, Context.MODE_PRIVATE)

        setView()

        adapters()

        listeners()

    }

    private fun setView() {
        if(sharedPreferences.getString(MainActivity.ACCOUNT_NAME,"")?.isEmpty() == true) {
            binding.registerSaveButton.visibility = View.VISIBLE
            binding.registerEditButton.visibility = View.GONE
            binding.registerBack.visibility = View.GONE


        } else {
            binding.registerSaveButton.visibility = View.GONE
            binding.registerEditButton.visibility = View.VISIBLE
            binding.registerBack.visibility = View.VISIBLE

            setViewsEditable(false)

            binding.registerName.itemInput.setText(sharedPreferences.getString(MainActivity.ACCOUNT_NAME,""))
            binding.registerAge.itemInput.setText(sharedPreferences.getString(MainActivity.ACCOUNT_AGE,""))
            binding.registerWeight.itemInput.setText(sharedPreferences.getString(MainActivity.ACCOUNT_WEIGHT,""))
            binding.registerHeight.itemInput.setText(sharedPreferences.getString(MainActivity.ACCOUNT_HEIGHT,""))
            binding.registerDietaryRestrictions.itemInput.setText(sharedPreferences.getString(MainActivity.ACCOUNT_DIETARY_RESTRICTIONS,""))
            binding.registerPreferredCuisineType.setText(sharedPreferences.getString(MainActivity.ACCOUNT_PREFERRED_CUISINE,""))
        }
    }

    fun setViewsEditable(editable: Boolean) {
        setViewEditable(binding.registerName.itemInput,editable)
        setViewEditable(binding.registerAge.itemInput,editable)
        setViewEditable(binding.registerHeight.itemInput,editable)
        setViewEditable(binding.registerWeight.itemInput,editable)
        setViewEditable(binding.registerDietaryRestrictions.itemInput,editable)
        setViewEditable(binding.layout,editable)
        setViewEditable(binding.registerPreferredCuisineTypeConstraint,editable)
    }

    fun setViewEditable(view :View, editable: Boolean) {
        view.apply {
            isFocusable = editable
            isFocusableInTouchMode = editable
            isEnabled = editable
        }
    }

    private fun adapters() {
        resources.getStringArray(R.array.cuisine_list).forEach {
             cuisineList.add(it)
        }
        lastSelectedCuisine = cuisineList[0]

        binding.registerPreferredCuisineType.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                cuisineList
            )
        )
    }

    private fun listeners() {
        binding.registerBack.setOnClickListener {
            mainViewModel.backPressed()
        }

        binding.registerPreferredCuisineType.setOnItemClickListener { _, _, position, _ ->
            lastSelectedCuisine = resources.getString(R.string.all_cuisine)
        }

        binding.registerEditButton.setOnClickListener {
            binding.registerSaveButton.visibility = View.VISIBLE
            setViewsEditable(true)
        }

        binding.registerSaveButton.setOnClickListener {
            saveDetails()
        }
    }

    fun saveDetails() {
        if (name.isNotEmpty()) {
            if (age.isNotEmpty()) {
                if (weight.isNotEmpty()) {
                    if (height.isNotEmpty()) {
                        if (dietaryRestrictions.isNotEmpty()) {
                            setViewsEditable(false)
                            sharedPreferencesEditor.putString(MainActivity.ACCOUNT_NAME,name)
                            sharedPreferencesEditor.putString(MainActivity.ACCOUNT_AGE,age)
                            sharedPreferencesEditor.putString(MainActivity.ACCOUNT_WEIGHT,weight)
                            sharedPreferencesEditor.putString(MainActivity.ACCOUNT_HEIGHT,height)
                            sharedPreferencesEditor.putString(MainActivity.ACCOUNT_DIETARY_RESTRICTIONS,dietaryRestrictions)
                            sharedPreferencesEditor.putString(MainActivity.ACCOUNT_PREFERRED_CUISINE,lastSelectedCuisine)
                            sharedPreferencesEditor.apply()
                            Toast.makeText(requireContext(), "Data saved", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Please fill Dietary Restrictions", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "Please fill Height", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Please fill Weight", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Please fill Age", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Please fill Name", Toast.LENGTH_SHORT).show()
        }
    }
}