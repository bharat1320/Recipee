package com.project.recipee.ui.cart

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.project.recipee.R
import com.project.recipee.data.LocalDish
import com.project.recipee.database.AppDatabase
import com.project.recipee.databinding.CustomIngredientViewBinding
import com.project.recipee.databinding.FragmentCartBinding
import com.project.recipee.databinding.FragmentRecipeDetailBinding
import com.project.recipee.ui.MainActivity
import com.project.recipee.viewModel.MainViewModel
import com.project.recipee.viewModel.RecipeViewModel

class CartFragment : Fragment() {
    lateinit var binding : FragmentCartBinding
    lateinit var mainViewModel: MainViewModel
    lateinit var sharedPreferences: SharedPreferences
    var items = arrayListOf<String>()
    val sharedPreferencesEditor by lazy {
        sharedPreferences.edit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater,container,false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        sharedPreferences =  requireActivity().getSharedPreferences(MainActivity.cartSharedPreferencesName, Context.MODE_PRIVATE)
        sharedPreferences.getStringSet(MainActivity.CART_ARRAY,null)?.forEach{
            items.add(it)
        }

        listeners()

        setView()

    }

    private fun listeners() {
        binding.cartBack.setOnClickListener {
            mainViewModel.backPressed()
        }
    }

    private fun setView() {
        binding.cartItemsLayout.removeAllViews()
        items.forEach {
            val layout = CustomIngredientViewBinding.inflate(LayoutInflater.from(requireContext()))
            val start = it.substringBefore(":")
            val end = it.substringAfter(":")
            layout.ingredientName.text = Html.fromHtml("<font color ='#228B22'>$start</font> : $end")
            layout.ingredientRemoveFromCart.setOnClickListener { view ->
                items.remove(it)
                binding.cartItemsLayout.removeView(layout.root)
            }
            binding.cartItemsLayout.addView(layout.root)
        }
    }

    override fun onPause() {
        sharedPreferencesEditor.putStringSet(MainActivity.CART_ARRAY,items.toSet())
        sharedPreferencesEditor.apply()
        super.onPause()
    }

}