package com.example.bestpractices.dev.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bestpractices.dev.data.database.hero.HeroDao
import com.example.bestpractices.dev.data.database.hero.HeroEntity
import com.example.bestpractices.dev.data.database.matchs.PlayerMatchDao
import com.example.bestpractices.dev.data.database.matchs.PlayerMatchEntity

@Database(
    entities = [
        PlayerMatchEntity::class,
        HeroEntity::class],
    version = 2,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playerMatchDao(): PlayerMatchDao
    abstract fun heroDao(): HeroDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    // Для простоты миграции при изменении версии
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}