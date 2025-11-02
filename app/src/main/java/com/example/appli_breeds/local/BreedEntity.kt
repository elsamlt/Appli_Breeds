package com.example.appli_breeds.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "breed")
data class BreedEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val breedGroup: String?,
    val origin: String?,
    val lifeSpan: String?,
    val temperament: String?,
    val heightMetric: String?,
    val weightMetric: String?,
    val referenceImageId: String?,
    val imageUrl: String?
)