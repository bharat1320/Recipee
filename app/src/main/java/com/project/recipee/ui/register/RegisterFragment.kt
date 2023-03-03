package com.project.recipee.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.project.recipee.databinding.FragmentRegisterBinding
import com.project.recipee.viewModel.MainViewModel
import com.project.recipee.viewModel.RecipeViewModel

class RegisterFragment : Fragment() {
    lateinit var binding :FragmentRegisterBinding
    lateinit var vm :RecipeViewModel
    lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProvider(this)[RecipeViewModel::class.java]
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]



    }
}