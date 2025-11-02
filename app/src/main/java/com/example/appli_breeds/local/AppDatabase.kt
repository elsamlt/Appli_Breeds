package com.example.appli_breeds.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [FavouriteEntity::class, BreedEntity::class],
    version = 2, // ⬅️ bump
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouriteDao(): FavouriteDao
    abstract fun breedDao(): BreedDao
}