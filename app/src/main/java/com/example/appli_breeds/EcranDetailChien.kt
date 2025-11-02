package com.example.appli_breeds

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.appli_breeds.model.Chien

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcranDetailChien(chien: Chien, onRetour: () -> Unit) {
    val scrollState = rememberLazyListState()
    val maxImageHeight = 280.dp
    val minImageHeight = 80.dp


    val imageHeight by remember {
        derivedStateOf {
            val offset = scrollState.firstVisibleItemScrollOffset.toFloat()
            val diff = (maxImageHeight - (offset / 3).dp)
            diff.coerceIn(minImageHeight, maxImageHeight)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(chien.name) },
                navigationIcon = { IconButton(onClick = onRetour) { Icon(Icons.Filled.ArrowBack, contentDescription = "Retour") } }
            )
        }
    ) { padding ->
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                AsyncImage(
                    model = chien.imageUrl ?: chien.image?.url,
                    contentDescription = chien.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeight),
                    contentScale = ContentScale.Crop
                )
            }
            item {
                Column(Modifier.padding(16.dp)) {
                    Text(chien.name, style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(8.dp))
                    InfoTexte("Groupe", chien.breed_group)
                    InfoTexte("Origine", chien.origin)
                    InfoTexte("Durée de vie", chien.life_span)
                    InfoTexte("Taille", chien.height.metric + " cm")
                    InfoTexte("Poids", chien.weight.metric + " kg")
                    InfoTexte("Tempérament", chien.temperament)
                    Spacer(Modifier.height(16.dp))
                    Text("Description", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(4.dp))
                    Text(chien.description.ifBlank { "Pas de description disponible." })
                    Spacer(Modifier.height(16.dp))
                    Text("Histoire", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(4.dp))
                    Text(chien.history.ifBlank { "Aucune histoire disponible." })
                }
            }
        }
    }
}

@Composable
fun InfoTexte(label: String, value: String) {
    if (value.isNotBlank()) {
        Row { Text("$label : ", style = MaterialTheme.typography.bodyMedium); Text(value, style = MaterialTheme.typography.bodyMedium) }
    }
}