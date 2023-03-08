package com.project.recipee.ui.recipeDetail

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.project.recipee.data.*
import com.project.recipee.database.AppDatabase
import com.project.recipee.databinding.CustomChipBinding
import com.project.recipee.databinding.FragmentRecipeDetailBinding
import com.project.recipee.ui.MainActivity
import com.project.recipee.util.DownloadImageTask
import com.project.recipee.viewModel.MainViewModel
import com.project.recipee.viewModel.RecipeViewModel
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class RecipeDetailFragment : Fragment() {
    lateinit var binding : FragmentRecipeDetailBinding
    lateinit var mainViewModel: MainViewModel
    lateinit var vm :RecipeViewModel

    private lateinit var appDb : AppDatabase
    lateinit var sharedPreferences: SharedPreferences
    var items = arrayListOf<String>()
    val sharedPreferencesEditor by lazy {
        sharedPreferences.edit()
    }

    val localDish = LocalDish(0,"","", arrayListOf(), arrayListOf(),"")

    val borderMargin = "0.0"
    val contentSize = "0.8"
    val start = "<html lang=\"en\">\n" +
            " <head>\n" +
            "<meta charset=\"UTF-8\" />\n" +
            "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n" +
            " <meta name=\"viewport\" content=\"width=device-width, initial-scale=$contentSize\" />\n" +
            " </head>\n" +
            " <body> <div style=\"margin: ${borderMargin}rem\">"
    val end = "</div></body></html>"

    companion object{
        val bundle_dishId = "bundle_dishId"
        val bundle_dishImage = "bundle_dishImage"
        val bundle_dishName = "bundle_dishName"

        val bundle_nutrition = "bundle_nutrition"
        val bundle_ingredients = "bundle_ingredients"
        val bundle_recipee = "bundle_recipee"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipeDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm = ViewModelProvider(requireActivity())[RecipeViewModel::class.java]
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        appDb = AppDatabase.getDatabaseInstance(activity as MainActivity)

        sharedPreferences =  requireActivity().getSharedPreferences(MainActivity.CART_SHARED_PREFERENCE, Context.MODE_PRIVATE)
        sharedPreferences.getStringSet(MainActivity.CART_ARRAY,null)?.forEach{
            items.add(it)
        }

        binding.recipeDetailRecipeWebView.settings.apply {
            loadWithOverviewMode = true
            useWideViewPort = true
            builtInZoomControls = true
            setSupportZoom(false)
            javaScriptEnabled = true
        }

        getData()

        observer()

        listeners()
    }

    fun getData() {
        arguments?.let {
            if(it.containsKey(bundle_dishId) &&
                it.containsKey(bundle_dishName) &&
                it.containsKey(bundle_dishImage)
            ) {
                localDish.id = it.getInt(bundle_dishId,0)
                localDish.title = it.getString(bundle_dishName,"")
                localDish.image = it.getString(bundle_dishImage,"")

                Glide.with(requireContext()).load(localDish.image).into(binding.recipeDetailImage)
                binding.recipeDetailName.text = localDish.title

                if(it.containsKey(bundle_ingredients) &&
                    it.containsKey(bundle_nutrition) &&
                    it.containsKey(bundle_recipee))
                {
                    binding.recipeDetailStar.playAnimation()
                    localDish.ingredientList = it.getStringArrayList(bundle_ingredients)
                    localDish.recipee = it.getString(bundle_recipee)
                    localDish.nutrition =it.getStringArrayList(bundle_nutrition)
                    setNutrientInView(localDish.nutrition)
                    setRecipeInView(localDish.recipee ?: "")
                    localDish.ingredientList?.forEach {
                        setIngredientInView(it)
                    }
                } else {
                    setLike(binding.recipeDetailStar, false)
                    vm.getIngredientsList(localDish.id)
                    vm.getRecipeInstructions(localDish.id)
                    vm.getNutritionalValue(localDish.id)
//                    vm.getRecipeData("https://api.spoonacular.com/recipes/641166/nutritionLabel")
                }
            } else {
                mainViewModel.backPressed()
            }
        }

    }

    fun observer() {
//        vm.recipeData.observe(viewLifecycleOwner) {
////            binding.recipeDetailRecipeWebView.loadData(it, "text/html","UTF-8")
//            binding.recipeDetailRecipeWebView.loadUrl("https://api.spoonacular.com/recipes/641166/nutritionLabel?apiKey=edff5a27d2cc4585928837c7338201e5")
//        }

        vm.ingredientList.observe(viewLifecycleOwner) {
            localDish.ingredientList?.clear()
            binding.recipeDetailIngredients.removeAllViews()
            it.forEach { ing ->
                val data = ing.getIngredientSimple()
                localDish.ingredientList?.add(data)
                setIngredientInView(data)
            }
        }

        vm.recipeInstructions.observe(viewLifecycleOwner) {
            localDish.recipee = it
            setRecipeInView(it)
        }

        vm.nutritionalValue.observe(viewLifecycleOwner) {
            binding.recipeDetailNutrients.removeAllViews()
            localDish.nutrition = arrayListOf()
            localDish.nutrition?.add("Protein ${it.protein?:"0gm"}")
            localDish.nutrition?.add("Calories : ${it.calories?:"0gm"}")
            localDish.nutrition?.add("Carbs : ${it.carbs?:"0gm"}")
            localDish.nutrition?.add("Fats : ${it.fat?:"0gm"}")
            if(localDish.nutrition?.isEmpty() == true) {
                localDish.nutrition?.clear()
                binding.text3.visibility = View.INVISIBLE
                binding.recipeDetailNutrients.visibility = View.GONE
            } else {
                setNutrientInView(localDish.nutrition)
            }
        }
    }

    fun listeners() {
        binding.recipeDetailBack.setOnClickListener {
            mainViewModel.backPressed()
        }

        binding.recipeDetailStar.setOnClickListener { view ->
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Are you sure you want to remove this from bookmark.." )
            builder.setCancelable(true)
            builder.setPositiveButton("Yes") { dialog, id ->
                vm.refreshBookmarkPage.postValue(true)
                setLike(binding.recipeDetailStar,false)
                vm.removeFromBookmark(appDb,localDish)
                dialog.cancel()
            }
            builder.setNegativeButton("No") { dialog, id ->
                dialog.cancel()
            }
            val alert: AlertDialog = builder.create()
            alert.show()
        }

        binding.recipeDetailStarBackground.setOnClickListener { view->
            vm.refreshBookmarkPage.postValue(true)
            setLike(binding.recipeDetailStar,true)

            if(!localDish.image.contains("http")) {
                //store the image in local storage
                val imageUrl = localDish.image
                val downloadTask = DownloadImageTask(requireContext(), imageUrl)
                val filePath = downloadTask.execute().get()
                val file = File(filePath)
                val uri = FileProvider.getUriForFile(
                    requireContext(),
                    "${(activity as MainActivity).packageName}.provider",
                    file
                )

                localDish.image = uri.toString()
            }

            vm.addToBookmark(appDb,localDish)
        }
    }

    fun setLike(likedAnimationView: LottieAnimationView, liked: Boolean) {
        if(liked) {
            likedAnimationView.visibility = View.VISIBLE
            likedAnimationView.playAnimation()
        } else {
            likedAnimationView.visibility = View.GONE
        }
    }

    fun setIngredientInView(it :String) {
        val layout = CustomChipBinding.inflate(LayoutInflater.from(requireContext()))
        layout.chipText.text = it
        layout.chipImage.visibility = View.VISIBLE
        layout.chipText.setOnClickListener { view ->
            addToCart(it)
        }
        layout.chipLayout.setOnClickListener { view ->
            addToCart(it)
        }
        layout.chipImage.setOnClickListener { view ->
            addToCart(it)
        }
        binding.recipeDetailIngredients.addView(layout.root)
    }

    fun addToCart(item :String) {
        items.add(localDish.title + " : " + item)
        Toast.makeText(requireContext(), "$item is added to your Shopping List", Toast.LENGTH_SHORT).show()
    }

    fun setRecipeInView(it :String) {
        binding.recipeDetailRecipeWebView.loadData("$start$it<br>$end", "text/html","UTF-8")
    }

    @SuppressLint("SetTextI18n")
    fun setNutrientInView(dataList: ArrayList<String>?) {
        dataList?.forEach {
            val layout = CustomChipBinding.inflate(LayoutInflater.from(requireContext()))
            layout.chipText.text = it
            layout.chipImage.visibility = View.GONE
            binding.recipeDetailNutrients.addView(layout.root)
        }
    }

    override fun onPause() {
        sharedPreferencesEditor.putStringSet(MainActivity.CART_ARRAY,items.toSet())
        sharedPreferencesEditor.apply()
        super.onPause()
    }
}
