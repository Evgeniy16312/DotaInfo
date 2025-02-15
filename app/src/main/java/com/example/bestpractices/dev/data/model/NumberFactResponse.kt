package com.example.bestpractices.dev.data.model

import com.google.gson.annotations.SerializedName

data class NumberFactResponse(
    @SerializedName("text") val text: String,
    @SerializedName("number") val number: Int,
    @SerializedName("found") val found: Boolean,
    @SerializedName("type") val type: String
)