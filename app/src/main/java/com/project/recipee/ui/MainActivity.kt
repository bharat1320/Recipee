package com.project.recipee.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.project.recipee.R
import com.project.recipee.databinding.ActivityMainBinding
import com.project.recipee.ui.home.HomeFragment
import com.project.recipee.ui.register.RegisterFragment
import com.project.recipee.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var sharedPreferences: SharedPreferences

    companion object{

        val message_tag = "/@/"

        fun LOG(data :String) {
            Log.d(message_tag,data)
        }

        val ACCOUNT_SHARED_PREFERENCE = "ACCOUNT_SHARED_PREFERENCE"
        val ACCOUNT_NAME = "ACCOUNT_NAME"
        val ACCOUNT_AGE = "ACCOUNT_AGE"
        val ACCOUNT_WEIGHT = "ACCOUNT_WEIGHT"
        val ACCOUNT_HEIGHT = "ACCOUNT_HEIGHT"
        val ACCOUNT_DIETARY_RESTRICTIONS = "ACCOUNT_DIETARY_RESTRICTIONS"
        val ACCOUNT_PREFERRED_CUISINE = "ACCOUNT_PREFERRED_CUISINE"

        val CART_SHARED_PREFERENCE = "CART_SHARED_PREFERENCE"
        val CART_ARRAY = "CART_ARRAY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        observer()

        setView()

    }

    private fun setView() {
        sharedPreferences = getSharedPreferences(ACCOUNT_SHARED_PREFERENCE, Context.MODE_PRIVATE)
        if(sharedPreferences.getString(ACCOUNT_NAME,"")?.isEmpty() == true) {
            loadFragment(RegisterFragment())
        } else {
            loadFragment(HomeFragment())
        }
    }

    fun observer() {
        mainViewModel.callFragment.observe(this) {
            if(it != null) {
                loadFragment(it)
            }
        }

        mainViewModel.backPressed.observe(this) {
            onBackPressed()
        }
    }

    @SuppressLint("WrongConstant")
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
            add(R.id.main_fragment_container, fragment)
            addToBackStack(null)
            commit()
        }

        LOG("FragmentOpen :- ${fragment::class.simpleName.toString()}")
    }

    // get current fragment displayed
    override fun onBackPressed() {
        val f = supportFragmentManager.findFragmentById(R.id.main_fragment_container).toString()
        if(f.contains(HomeFragment::class.simpleName.toString()) ||
            (f.contains(RegisterFragment::class.simpleName.toString()) && !sharedPreferences.contains(ACCOUNT_NAME))) {
            this.finishAffinity()
        } else {
            super.onBackPressed()
        }
    }
}