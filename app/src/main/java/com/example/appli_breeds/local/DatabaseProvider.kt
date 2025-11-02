package com.example.appli_breeds.local

import android.content.Context
import androidx.room.Room


object DatabaseProvider {
    @Volatile private var INSTANCE: AppDatabase? = null


    fun get(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
        INSTANCE ?: Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "dogs.db"
        )
            .fallbackToDestructiveMigration()
            .build()
            .also { INSTANCE = it }
    }
}