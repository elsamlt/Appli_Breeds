package com.example.appli_breeds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appli_breeds.BreedsRepository
import com.example.appli_breeds.local.AppDatabase
import com.example.appli_breeds.local.LocalBreedsDataSource
import com.example.appli_breeds.model.Chien
import com.example.appli_breeds.model.imageIdForFavourite
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class DogViewModel(
    private val subId: String,
    private val db: AppDatabase,
    private val remoteRepo: DogRepository = DogRepository()
) : ViewModel() {


    // ----- Local repos -----
    private val breedsRepo = BreedsRepository(remoteRepo, LocalBreedsDataSource(db.breedDao()))
    private val localFav = com.example.appli_breeds.local.LocalFavouritesDataSource(db.favouriteDao())


    // ----- RACES -----
    private val _visibleBreeds = MutableStateFlow<List<Chien>>(emptyList())
    val visibleBreeds: StateFlow<List<Chien>> = _visibleBreeds


    val dogs: StateFlow<List<Chien>> = breedsRepo.observeAll()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


    fun loadDogs() {
        viewModelScope.launch { runCatching { breedsRepo.refreshFromRemote() } }
    }

    fun searchOrAll(query: String) {
        if (query.isBlank()) {
            viewModelScope.launch {
                breedsRepo.observeAll().collect { _visibleBreeds.value = it }
            }
        } else {
            viewModelScope.launch {
                breedsRepo.search(query).collect { _visibleBreeds.value = it }
            }
        }
    }


    fun getDogById(id: Int): Chien? = dogs.value.firstOrNull { it.id == id }


    // ----- FAVORIS (identique, mais basé sur Room pour l'état) -----
    val favMap: StateFlow<Map<String, Int?>> = localFav.favouritesIdsFlow
        .map { ids -> ids.associateWith { null } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyMap())


    private fun refreshFavouritesFromRemote() {
        viewModelScope.launch {
            runCatching { remoteRepo.listFavourites(subId) }
                .onSuccess { favs -> localFav.setAllFromRemote(favs.map { it.image_id to it.id }) }
        }
    }

    fun toggleFavourite(chien: Chien) {
        val imageId = chien.imageIdForFavourite() ?: return
        val isFav = favMap.value.containsKey(imageId)
        viewModelScope.launch {
            if (!isFav) {
                val resp = runCatching { remoteRepo.createFavourite(imageId, subId) }.getOrNull()
                localFav.add(imageId, resp?.id)
            } else {
                val serverId = favMap.value[imageId]
                runCatching { if (serverId != null) remoteRepo.deleteFavourite(serverId) }
                localFav.remove(imageId)
            }
        }
    }


    init {
// synchro favoris au lancement
        refreshFavouritesFromRemote()
// peupler visibleBreeds au démarrage
        viewModelScope.launch { breedsRepo.observeAll().collect { _visibleBreeds.value = it } }
    }
}