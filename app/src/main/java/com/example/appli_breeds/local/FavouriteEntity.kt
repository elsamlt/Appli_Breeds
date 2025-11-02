package com.example.appli_breeds.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favourite")
data class FavouriteEntity(
    @PrimaryKey val imageId: String, // identifiant attendu par TheDogAPI
    val serverId: Int? = null, // id du favouri coté serveur (pour DELETE)
    val createdAt: Long = System.currentTimeMillis()
)