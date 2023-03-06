package com.project.recipee.data

import com.google.gson.annotations.SerializedName
import kotlin.math.roundToInt

data class Ingredients(  @SerializedName("ingredients") var ingredients   : ArrayList<Ingredient>? = null  )

data class Ingredient(  @SerializedName("name"   ) var name   : String? = null,
                        @SerializedName("image"  ) var image  : String? = null,
                        @SerializedName("amount" ) var amount : QuantityUnits? = QuantityUnits())

data class QuantityUnits(
    @SerializedName("metric" ) var metric : QuantityAmount?     = QuantityAmount(),
    @SerializedName("us"     ) var us     : QuantityAmount?     = QuantityAmount()
)

data class QuantityAmount (
    @SerializedName("value" ) var value : Double? = null,
    @SerializedName("unit"  ) var unit  : String? = null
)

fun Ingredient.getIngredientSimple() :String {
    return "${name} ${amount?.metric?.value?.roundToInt()}${amount?.metric?.unit}"
}