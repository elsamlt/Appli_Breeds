package com.example.appli_breeds

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.appli_breeds.model.Chien
import com.example.appli_breeds.model.imageIdForFavourite

@Composable
fun LigneChien(
    chien: Chien,
    onClick: () -> Unit,
    vm: DogViewModel
) {
    val favMap by vm.favMap.collectAsState()
    val isFav = remember(favMap, chien) { chien.imageIdForFavourite()?.let { favMap.containsKey(it) } == true }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        AsyncImage(
            model = chien.imageUrl,
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
        IconButton(onClick = { vm.toggleFavourite(chien) }) {
            if (isFav) Icon(Icons.Filled.Favorite, contentDescription = "Retirer des favoris")
            else Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Ajouter aux favoris")
        }
    }
}