package com.project.recipee.data

import com.google.gson.annotations.SerializedName

data class Recipee(
    @SerializedName("id"      ) var id      : Int?    = null,
    @SerializedName("title"   ) var title   : String? = null,
    @SerializedName("summary" ) var recipee : String? = null
)
