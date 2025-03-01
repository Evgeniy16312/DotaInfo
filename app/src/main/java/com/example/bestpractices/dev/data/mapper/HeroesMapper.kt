package com.example.bestpractices.dev.data.mapper

import com.example.bestpractices.dev.data.database.hero.HeroEntity
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

    fun mapEntityToDomain(heroEntities: List<HeroEntity>): List<Heroes> {
        return heroEntities.map { entity ->
            Heroes(
                id = entity.id,
                name = entity.name,
                localizedName = entity.localizedName,
                primaryAttr = entity.primaryAttr,
                attackType = entity.attackType,
                roles = entity.roles.split(","), // Предполагаем, что роли хранятся через запятую
                logo = entity.logo
            )
        }
    }

    fun mapDomainToEntity(heroes: List<Heroes>): List<HeroEntity> {
        return heroes.map { hero ->
            HeroEntity(
                id = hero.id,
                name = hero.name,
                localizedName = hero.localizedName,
                primaryAttr = hero.primaryAttr,
                attackType = hero.attackType,
                roles = hero.roles.joinToString(","), // Список ролей в строку
                logo = hero.logo
            )
        }
    }

    private fun generateLogoUrl(heroName: String): String {
        val heroNameFormatted = heroName.removePrefix("npc_dota_hero_")
        return "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/$heroNameFormatted.png"
    }
}