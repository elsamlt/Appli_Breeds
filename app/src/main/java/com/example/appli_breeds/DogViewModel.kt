package com.example.appli_breeds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appli_breeds.model.Chien
import com.example.appli_breeds.DogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DogViewModel : ViewModel() {
    private val repo = DogRepository()

    private val _dogs = kotlinx.coroutines.flow.MutableStateFlow<List<Chien>>(emptyList())
    val dogs: kotlinx.coroutines.flow.StateFlow<List<Chien>> = _dogs

    // Liste affichée quand on tape dans la barre de recherche
    private val _visibleBreeds = kotlinx.coroutines.flow.MutableStateFlow<List<Chien>>(emptyList())
    val visibleBreeds: kotlinx.coroutines.flow.StateFlow<List<Chien>> = _visibleBreeds

    fun loadDogs() {
        viewModelScope.launch {
            val all = repo.getChien()
            _dogs.value = all
            _visibleBreeds.value = all        // par défaut on montre tout
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
}
