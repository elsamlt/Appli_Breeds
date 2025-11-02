package com.example.appli_breeds.model

import kotlinx.serialization.Serializable


@Serializable
data class Chien(
    val bred_for: String = "",
    val breed_group: String = "",
    val country_code: String = "",
    val description: String = "",
    val height: Height = Height(),
    val history: String = "",
    val id: Int = 0,
    val life_span: String = "",
    val name: String = "",
    val origin: String = "",
    val reference_image_id: String = "",
    val temperament: String = "",
    val weight: Weight = Weight(),
    val image: DogImage = DogImage()
)
{
    val imageUrl: String?
        get() = reference_image_id?.let { "https://cdn2.thedogapi.com/images/$it.jpg" }
}

@Serializable
data class Height(
    val imperial: String = "",
    val metric: String = ""
)

@Serializable
data class Weight(
    val imperial: String = "",
    val metric: String = ""
)

@Serializable
class DogImage(
    val id: String = "",
    val url: String = "",
    val width: Int = 0,
    val height: Int = 0
)

fun Chien.imageIdForFavourite(): String? = image?.id ?: reference_image_id