package com.project.recipee.data

import kotlin.math.roundToInt

data class Nutrient(
    val name : String,
    val amount : Double,
    val unit : String
)

fun Nutrient.getNutritionString() :String {
    return name + ": ${amount.roundToInt()}$unit"
}
