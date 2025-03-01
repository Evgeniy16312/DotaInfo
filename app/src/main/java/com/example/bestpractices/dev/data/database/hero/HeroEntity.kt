package com.example.bestpractices.dev.data.database.hero

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "heroes")
data class HeroEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val localizedName: String?,
    val primaryAttr: String,
    val attackType: String,
    val roles: String,
    val logo: String?
)