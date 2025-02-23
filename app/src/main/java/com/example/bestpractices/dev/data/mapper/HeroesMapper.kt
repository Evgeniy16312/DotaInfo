package com.example.bestpractices.dev.data.mapper

import com.example.bestpractices.dev.data.model.HeroesResponse
import com.example.bestpractices.dev.domain.model.Heroes

class HeroesMapper {
    fun mapResponseToDomain(heroesResponse: List<HeroesResponse>): List<Heroes> {
        return heroesResponse.map { response ->
            Heroes(
                id = response.id,
                name = response.name,
                localizedName = response.localizedName ?: "Unknown",
                primaryAttr = response.primaryAttr,
                attackType = response.attackType,
                roles = response.roles,
                logo = generateLogoUrl(response.name)
            )
        }
    }

    private fun generateLogoUrl(heroName: String): String {
        // Преобразуем имя героя в часть URL
        val heroNameFormatted = heroName.removePrefix("npc_dota_hero_")
        return "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/$heroNameFormatted.png"
    }
}