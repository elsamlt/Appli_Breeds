package com.example.appli_breeds

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.appli_breeds.model.Chien

// --- Destinations
sealed class Destination
object ListeRaces : Destination()
data class DetailRace(val chien: Chien) : Destination()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AppliChiens() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppliChiens() {
    val backStack = remember { mutableStateListOf<Destination>(ListeRaces) }
    val viewModel: DogViewModel = viewModel()
    val chiens by viewModel.dogs.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        viewModel.loadDogs()
    }

    when (val top = backStack.last()) {
        is ListeRaces -> EcranListeChiens(
            chiens = chiens,
            onChienClick = { backStack.add(DetailRace(it)) }
        )
        is DetailRace -> EcranDetailChien(
            chien = top.chien,
            onRetour = { backStack.removeLastOrNull() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcranListeChiens(
    chiens: List<Chien>,
    onChienClick: (Chien) -> Unit
) {
    Scaffold(
        topBar = {
            Surface(
                tonalElevation = 2.dp,
                shadowElevation = 3.dp
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Dog Breeds",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        AsyncImage(
                            model = "https://cdn-icons-png.flaticon.com/512/194/194938.png",
                            contentDescription = "Profil",
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .size(36.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    },
                    actions = {
                        IconButton(onClick = { /* notifications */ }) {
                            Icon(
                                imageVector = Icons.Filled.Notifications,
                                contentDescription = "Notifications",
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                    }
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(chiens) { chien ->
                LigneChien(chien = chien, onClick = { onChienClick(chien) })
                Divider()
            }
        }
    }
}

@Composable
fun TopAppBar(
    title: () -> Unit,
    navigationIcon: () -> Unit,
    actions: () -> Unit
) {
    TODO("Not yet implemented")
}

@Composable
fun LigneChien(chien: Chien, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = chien.imageUrl ?: chien.image.url,
            contentDescription = chien.name,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(chien.name, style = MaterialTheme.typography.titleMedium)
            Text(
                chien.breed_group,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcranDetailChien(chien: Chien, onRetour: () -> Unit) {
    val scrollState = rememberLazyListState()
    val maxImageHeight = 280.dp
    val minImageHeight = 80.dp

    // Hauteur dynamique pour effet “disparition”
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
                navigationIcon = {
                    IconButton(onClick = onRetour) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Retour"
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Image principale (hauteur variable)
            item {
                AsyncImage(
                    model = chien.imageUrl ?: chien.image.url,
                    contentDescription = chien.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeight),
                    contentScale = ContentScale.Crop
                )
            }

            // Détails du chien
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
        Row {
            Text("$label : ", style = MaterialTheme.typography.bodyMedium)
            Text(value, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
