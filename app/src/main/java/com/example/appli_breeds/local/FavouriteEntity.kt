package com.example.appli_breeds.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favourite")
data class FavouriteEntity(
    @PrimaryKey val imageId: String,
    val serverId: Int? = null,
    val createdAt: Long = System.currentTimeMillis()
)