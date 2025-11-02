package com.example.appli_breeds.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface BreedDao {
    @Query("SELECT * FROM breed ORDER BY name")
    fun getAllFlow(): Flow<List<BreedEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(entities: List<BreedEntity>)


    @Query("DELETE FROM breed")
    suspend fun clearAll()


    @Query("SELECT * FROM breed WHERE name LIKE '%' || :q || '%' ORDER BY name")
    fun searchByNameFlow(q: String): Flow<List<BreedEntity>>
}