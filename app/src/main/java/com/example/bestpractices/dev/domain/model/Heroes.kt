package com.example.bestpractices.dev.domain.model

data class Heroes(
    val id: Int,
    val name: String,
    val localizedName: String?,
    val primaryAttr: String,
    val attackType: String,
    val roles: List<String>,
    val logo: String?
)