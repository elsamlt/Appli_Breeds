package com.example.appli_breeds.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class LocalFavouritesDataSource(private val dao: FavouriteDao) {
    val favouritesIdsFlow: Flow<Set<String>> = dao.getAllImageIdsFlow().map { it.toSet() }


    suspend fun setAllFromRemote(pairs: List<Pair<String, Int?>>) {
// Remplace le contenu local par l'état serveur
        dao.clearAll()
        dao.upsertAll(pairs.map { (imgId, serverId) -> FavouriteEntity(imageId = imgId, serverId = serverId) })
    }


    suspend fun add(imageId: String, serverId: Int?) {
        dao.upsert(FavouriteEntity(imageId = imageId, serverId = serverId))
    }


    suspend fun remove(imageId: String) {
        dao.deleteByImageId(imageId)
    }
}