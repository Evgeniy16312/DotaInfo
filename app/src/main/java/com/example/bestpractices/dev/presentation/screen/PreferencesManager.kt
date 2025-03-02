package com.example.bestpractices.dev.presentation.screen

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {
    private val prefs = context.getSharedPreferences("DotaInfoPrefs", Context.MODE_PRIVATE)
    private val KEY_STEAM_ID = "steam_id"

    fun saveSteamId(steamId: Long) {
        prefs.edit { putLong(KEY_STEAM_ID, steamId) }
    }

    fun getSteamId(): Long? {
        val id = prefs.getLong(KEY_STEAM_ID, -1L)
        return if (id == -1L) null else id
    }

    fun clearSteamId() {
        prefs.edit { remove(KEY_STEAM_ID) }
    }
}