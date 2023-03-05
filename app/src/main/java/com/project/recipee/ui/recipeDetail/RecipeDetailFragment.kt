package com.project.recipee.ui.recipeDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.project.recipee.R
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

        binding.webview.settings.apply {
            loadWithOverviewMode = true
            useWideViewPort = true
            builtInZoomControls = true
            setSupportZoom(true)
        }

        getData()

        observer()
    }

    fun getData() {
        vm.getRecipeData("https://api.spoonacular.com/recipes/1082038/ingredientWidget")
    }

    fun observer() {
        vm.recipeData.observe(viewLifecycleOwner) {
            binding.webview.loadData(
                start+it+end,"text/html","UTF-8"
            )

//            binding.webview.loadData("<html>$it</html>","text/html","UTF-8")
        }
    }
}