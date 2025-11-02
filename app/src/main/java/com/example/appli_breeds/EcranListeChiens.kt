package com.example.appli_breeds

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.appli_breeds.model.Chien

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcranListeChiens(
    chiens: List<Chien>,
    vm: DogViewModel,
    onChienClick: (Chien) -> Unit,
    onAvatarClick: () -> Unit
){
    var query by rememberSaveable { mutableStateOf("") }
    val visible by vm.visibleBreeds.collectAsState()


// Debounce
    LaunchedEffect(query) {
        kotlinx.coroutines.delay(300)
        vm.searchOrAll(query)
    }

    Scaffold(
        topBar = {
            Surface(tonalElevation = 2.dp, shadowElevation = 3.dp) {
                CenterAlignedTopAppBar(
                    title = { Text("Dog Breeds", style = MaterialTheme.typography.titleLarge) },
                    navigationIcon = {
                        AsyncImage(
                            model = "https://cdn-icons-png.flaticon.com/512/194/194938.png",
                            contentDescription = "Profil",
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .size(36.dp)
                                .clip(CircleShape)
                                .clickable { onAvatarClick() },
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
// Barre de recherche (plus fine)
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = "Search") },
                placeholder = { Text("Rechercher une race") },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .heightIn(min = 44.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
                )
            )

            val data = if (query.isBlank()) chiens else visible


            if (data.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(Modifier.fillMaxSize()) {
                    items(data) { chien ->
                        LigneChien(chien = chien, onClick = { onChienClick(chien) }, vm = vm)
                        Divider()
                    }
                }
            }
        }
    }
}