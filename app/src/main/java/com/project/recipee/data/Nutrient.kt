package com.project.recipee.data

import com.google.gson.annotations.SerializedName
import kotlin.math.roundToInt

data class Nutrient(
    @SerializedName("name") val name : String,
    @SerializedName("amount") val amount : Double,
    @SerializedName("unit") val unit : String
)

fun Nutrient.getNutritionSimple() :String {
    return name + ": ${amount.roundToInt()}$unit"
}
