package com.example.appli_breeds

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
    chiens: List<Chien>,                // liste complète (loadDogs)
    onChienClick: (Chien) -> Unit
) {
    val vm: DogViewModel = viewModel()

    var query by rememberSaveable { mutableStateOf("") }
    val visible by vm.visibleBreeds.collectAsState()  // liste affichée (search ou all)
    val scope = rememberCoroutineScope()

    // Debounce: lance la recherche 300ms après la dernière frappe
    LaunchedEffect(query) {
        kotlinx.coroutines.delay(300)
        vm.searchOrAll(query)
    }

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
                    }
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // ---- Barre Search + bouton filtre (style bulle arrondie) ----
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search"
                        )
                    },
                    placeholder = { Text("Search") },
                    singleLine = true,
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    )
                )

                Spacer(Modifier.width(10.dp))

                IconButton(
                    onClick = { /* plus tard: filtre (groupe, taille, etc.) */ },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Tune,
                        contentDescription = "Filter"
                    )
                }
            }

            // ---- Liste (affiche la recherche si query non vide, sinon la liste complète) ----
            val data = if (query.isBlank()) chiens else visible

            if (data.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(Modifier.fillMaxSize()) {
                    items(data) { chien ->
                        LigneChien(chien = chien, onClick = { onChienClick(chien) })
                        Divider()
                    }
                }
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
