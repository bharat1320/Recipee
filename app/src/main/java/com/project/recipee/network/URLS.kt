package com.project.recipee.network

class URLS {
    companion object {
        const val headerName = "x-api-key"
        const val headerValue = "edff5a27d2cc4585928837c7338201e5"

        const val complexSearch = "complexSearch"

//        GET
        const val getIngredientList = "/ingredientWidget.json"
        const val getRecipeInstructions = "/summary"
        const val getNutritionalValue = "/nutritionWidget.json"
    }
}