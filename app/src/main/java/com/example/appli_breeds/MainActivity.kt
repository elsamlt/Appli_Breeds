package com.example.appli_breeds


import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appli_breeds.model.Chien


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AppliChiens() }
    }
}


@Composable
fun AppliChiens() {
    val backStack = remember { mutableStateListOf<Destination>(ListeRaces) }


// subId stable par appareil
    val ctx = LocalContext.current
    val subId = remember {
        Settings.Secure.getString(ctx.contentResolver, Settings.Secure.ANDROID_ID) ?: "unknown-device"
    }


// ViewModel unique injecté
    val vm: DogViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DogViewModel(subId = subId) as T
            }
        }
    )


    val chiens by vm.dogs.collectAsState(initial = emptyList())
    LaunchedEffect(Unit) { vm.loadDogs() }


    when (val top = backStack.last()) {
        is ListeRaces -> EcranListeChiens(
            chiens = chiens,
            vm = vm,
            onChienClick = { backStack.add(DetailRace(it)) },
            onAvatarClick = { backStack.add(ListeFavoris) }
        )
        is ListeFavoris -> EcranFavoris(
            vm = vm,
            onBack = { backStack.removeLastOrNull() },
            onChienClick = { backStack.add(DetailRace(it)) }
        )
        is DetailRace -> EcranDetailChien(
            chien = top.chien,
            onRetour = { backStack.removeLastOrNull() }
        )
    }
}

// ajouter le truc en local du coup
//trier l'appli