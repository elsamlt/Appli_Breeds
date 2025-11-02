package com.example.appli_breeds

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.appli_breeds.model.Chien
import com.example.appli_breeds.model.imageIdForFavourite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcranFavoris(onBack: () -> Unit, onChienClick: (Chien) -> Unit, vm: DogViewModel) {
    val dogs by vm.dogs.collectAsState()
    val favMap by vm.favMap.collectAsState()
    val favoris = remember(dogs, favMap) { dogs.filter { it.imageIdForFavourite()?.let(favMap::containsKey) == true } }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mes favoris") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Retour") } }
            )
        }
    ) { padding ->
        if (favoris.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Aucun favori pour l’instant.")
            }
        } else {
            LazyColumn(Modifier.fillMaxSize().padding(padding)) {
                items(favoris) { chien ->
                    LigneChien(chien = chien, onClick = { onChienClick(chien) }, vm = vm)
                    Divider()
                }
            }
        }
    }
}