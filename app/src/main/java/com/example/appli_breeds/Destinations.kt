package com.example.appli_breeds

import com.example.appli_breeds.model.Chien

sealed class Destination
object ListeRaces : Destination()
object ListeFavoris : Destination()
data class DetailRace(val chien: Chien) : Destination()