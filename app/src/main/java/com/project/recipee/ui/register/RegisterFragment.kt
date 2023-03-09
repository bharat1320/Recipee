package com.project.recipee.ui.register

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        sharedPreferences =  requireActivity().getSharedPreferences(MainActivity.ACCOUNT_SHARED_PREFERENCE, Context.MODE_PRIVATE)



    }

    fun saveDetails() {
        sharedPreferencesEditor.putString(MainActivity.CART_ARRAY,"jjjj")

        sharedPreferencesEditor.apply()
        super.onPause()
    }
}