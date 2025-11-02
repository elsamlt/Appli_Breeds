package com.example.appli_breeds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appli_breeds.model.Chien
import com.example.appli_breeds.DogRepository
import com.example.appli_breeds.model.imageIdForFavourite
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DogViewModel(
    private val subId: String, // injecté (ANDROID_ID)
    private val repo: DogRepository = DogRepository()
) : ViewModel() {


    private val _dogs = MutableStateFlow<List<Chien>>(emptyList())
    val dogs: StateFlow<List<Chien>> = _dogs


    private val _visibleBreeds = MutableStateFlow<List<Chien>>(emptyList())
    val visibleBreeds: StateFlow<List<Chien>> = _visibleBreeds


    // image_id -> favouriteId
    private val _favMap = MutableStateFlow<Map<String, Int>>(emptyMap())
    val favMap: StateFlow<Map<String, Int>> = _favMap


    fun loadDogs() {
        viewModelScope.launch {
            val all = repo.getChien()
            _dogs.value = all
            _visibleBreeds.value = all
            refreshFavourites()
        }
    }


    fun searchOrAll(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                _visibleBreeds.value = _dogs.value
            } else {
                runCatching { repo.searchBreeds(query) }
                    .onSuccess { _visibleBreeds.value = it }
                    .onFailure { _visibleBreeds.value = emptyList() }
            }
        }
    }


    fun getDogById(id: Int): Chien? = _dogs.value.firstOrNull { it.id == id }


    fun refreshFavourites() {
        viewModelScope.launch {
            runCatching { repo.listFavourites(subId) }
                .onSuccess { favs -> _favMap.value = favs.associate { it.image_id to it.id } }
                .onFailure { _favMap.value = emptyMap() }
        }
    }


    fun isFavourite(chien: Chien): Boolean {
        val imgId = chien.imageIdForFavourite() ?: return false
        return _favMap.value.containsKey(imgId)
    }


    fun toggleFavourite(chien: Chien) {
        val imgId = chien.imageIdForFavourite() ?: return
        val existing = _favMap.value[imgId]
        viewModelScope.launch {
            if (existing == null) {
                runCatching { repo.createFavourite(imgId, subId) }
                    .onSuccess { resp ->
                        if (resp.id != null) _favMap.value = _favMap.value + (imgId to resp.id)
                        else refreshFavourites()
                    }
            } else {
                runCatching { repo.deleteFavourite(existing) }
                    .onSuccess { _favMap.value = _favMap.value - imgId }
            }
        }
    }
}
