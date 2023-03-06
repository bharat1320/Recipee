package com.project.recipee.ui.recipeDetail

import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.project.recipee.R
import com.project.recipee.data.Ingredient
import com.project.recipee.data.getIngredientSimple
import com.project.recipee.databinding.CustomChipBinding
import com.project.recipee.databinding.CustomIngredientViewBinding
import com.project.recipee.databinding.FragmentHomeBinding
import com.project.recipee.databinding.FragmentRecipeDetailBinding
import com.project.recipee.viewModel.MainViewModel
import com.project.recipee.viewModel.RecipeViewModel

class RecipeDetailFragment : Fragment() {
    lateinit var binding : FragmentRecipeDetailBinding
    lateinit var mainViewModel: MainViewModel
    lateinit var vm :RecipeViewModel

    val it = "Any random String"
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
        val bundle_dishId = "dishId"
        val bundle_dishImage = "dishImage"
        val bundle_dishName = "dishName"

        var dishId = 0
        var dishImage = ""
        var dishName = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipeDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm = ViewModelProvider(requireActivity())[RecipeViewModel::class.java]
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        binding.recipeDetailRecipeWebView.settings.apply {
            loadWithOverviewMode = true
            useWideViewPort = true
            builtInZoomControls = true
            setSupportZoom(true)
        }

        getData()

        observer()

        listeners()
    }

    fun getData() {
        arguments?.let {
            if(it.containsKey(bundle_dishId) &&
                it.containsKey(bundle_dishId) &&
                it.containsKey(bundle_dishId)
            ) {
                dishId = it.getInt(bundle_dishId,0)
                dishName = it.getString(bundle_dishName,"")
                dishImage = it.getString(bundle_dishImage,"")
            }
        }
        Glide.with(requireContext()).load(dishImage).into(binding.recipeDetailImage)
        binding.recipeDetailName.text = dishName
        setLike(binding.recipeDetailStar,false)

        vm.getRecipeData("https://api.spoonacular.com/recipes/$dishId/ingredientWidget")
        vm.getIngredientsList(dishId)
        vm.getRecipeInstructions(dishId)
    }

    fun observer() {
        vm.recipeData.observe(viewLifecycleOwner) {

        }

        vm.ingredientList.observe(viewLifecycleOwner) {
            setIngredientsInView(it)
        }

        vm.recipeInstructions.observe(viewLifecycleOwner) {
            binding.recipeDetailRecipeWebView.loadData("$start$it<br><br><br><br><br><br><br>$end", "text/html","UTF-8")
        }
    }

    fun listeners() {
        binding.recipeDetailBack.setOnClickListener {
            mainViewModel.backPressed.postValue(true)
        }

        binding.recipeDetailStar.setOnClickListener { view ->
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Are you sure you want to remove this from bookmark.." )
            builder.setCancelable(true)
            builder.setPositiveButton("Yes") { dialog, id ->
                setLike(binding.recipeDetailStar,false)
//                        listener.itemRemoveFromCartCLicked(item)
                dialog.cancel()
            }
            builder.setNegativeButton("No") { dialog, id ->
                dialog.cancel()
            }
            val alert: AlertDialog = builder.create()
            alert.show()
        }

        binding.recipeDetailStarBackground.setOnClickListener { view->
            setLike(binding.recipeDetailStar,true)
//                    listener.itemAddToCartCLicked(item)
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

    fun setIngredientsInView(data : ArrayList<Ingredient>) {
        binding.recipeDetailIngredients.removeAllViews()
        data.forEach {
            val layout = CustomChipBinding.inflate(LayoutInflater.from(requireContext()))
            layout.chipText.text = it.getIngredientSimple()
            layout.chipImage.visibility = View.VISIBLE
            layout.chipText.setOnClickListener {

            }
            layout.chipLayout.setOnClickListener {

            }
            layout.chipImage.setOnClickListener {

            }
            binding.recipeDetailIngredients.addView(layout.root)
        }
    }
}