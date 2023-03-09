package com.project.recipee.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.project.recipee.R
import com.project.recipee.databinding.ActivityMainBinding
import com.project.recipee.ui.home.HomeFragment
import com.project.recipee.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    companion object{
        fun LOG(data :String) {
            Log.d("/@/",data)
        }
        val message_tag = "/@/"

        val ACCOUNT_SHARED_PREFERENCE = "ACCOUNT_SHARED_PREFERENCE"

        val CART_SHARED_PREFERENCE = "CART_SHARED_PREFERENCE"
        val CART_ARRAY = "CART_ARRAY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        observer()

        loadFragment(HomeFragment())

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
        if(f.contains(HomeFragment::class.simpleName.toString())) {
            this.finishAffinity()
        } else {
            super.onBackPressed()
        }
    }
}