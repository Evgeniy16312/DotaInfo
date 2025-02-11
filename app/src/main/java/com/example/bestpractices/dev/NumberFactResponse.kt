package com.example.bestpractices.dev

import com.google.gson.annotations.SerializedName

data class NumberFactResponse(
    @SerializedName("text") val text: String,
    @SerializedName("number") val number: Int,
    @SerializedName("found") val found: Boolean,
    @SerializedName("type") val type: String
)
