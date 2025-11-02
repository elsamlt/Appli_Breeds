package com.example.appli_breeds.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface FavouriteDao {
    @Query("SELECT * FROM favourite")
    fun getAllFlow(): Flow<List<FavouriteEntity>>


    @Query("SELECT imageId FROM favourite")
    fun getAllImageIdsFlow(): Flow<List<String>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: FavouriteEntity)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(entities: List<FavouriteEntity>)


    @Query("DELETE FROM favourite WHERE imageId = :imageId")
    suspend fun deleteByImageId(imageId: String)


    @Query("DELETE FROM favourite")
    suspend fun clearAll()
}