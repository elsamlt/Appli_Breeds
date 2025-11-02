package com.example.appli_breeds

import com.example.appli_breeds.DogRepository
import com.example.appli_breeds.local.LocalBreedsDataSource
import com.example.appli_breeds.model.Chien
import kotlinx.coroutines.flow.Flow


class BreedsRepository(
    private val remote: DogRepository,
    private val local: LocalBreedsDataSource
) {
    fun observeAll(): Flow<List<Chien>> = local.allBreedsFlow


    fun search(q: String): Flow<List<Chien>> = local.searchByNameFlow(q)


    suspend fun refreshFromRemote() {
        val remoteList = remote.getChien()
        local.replaceAll(remoteList)
    }
}