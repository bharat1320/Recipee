package com.project.recipee.data

import com.google.gson.annotations.SerializedName

data class Nutrients(
    @SerializedName("calories" ) var calories : String?         = null,
    @SerializedName("carbs"    ) var carbs    : String?         = null,
    @SerializedName("fat"      ) var fat      : String?         = null,
    @SerializedName("protein"  ) var protein  : String?         = null
)