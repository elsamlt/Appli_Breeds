package com.example.appli_breeds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appli_breeds.model.Chien
import com.example.appli_breeds.DogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DogViewModel(
    private val repository: DogRepository = DogRepository()
) : ViewModel() {

    private val _dogs = MutableStateFlow<List<Chien>>(emptyList())
    val dogs: StateFlow<List<Chien>> = _dogs

    fun loadDogs() {
        viewModelScope.launch {
            _dogs.value = repository.getChien()
        }
    }
}