package com.project.recipee.data

import com.google.gson.annotations.SerializedName

data class PagingResponse <T> (
    @SerializedName("results"      ) var results      : ArrayList<T> = arrayListOf(),
    @SerializedName("offset"       ) var offset       : Int = 0,
    @SerializedName("number"       ) var number       : Int = 0,
    @SerializedName("totalResults" ) var totalResults : Int = 0
)
