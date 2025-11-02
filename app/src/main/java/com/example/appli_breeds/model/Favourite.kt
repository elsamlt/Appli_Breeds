package com.example.appli_breeds.model

import kotlinx.serialization.Serializable


@Serializable
data class Favourite(
    val id: Int,
    val image_id: String,
    val sub_id: String? = null,
    val created_at: String? = null,
    val image: FavouriteImage? = null
)


@Serializable
data class FavouriteImage(
    val id: String? = null,
    val url: String? = null
)


@Serializable
data class CreateFavouriteRequest(
    val image_id: String,
    val sub_id: String? = null
)


@Serializable
data class CreateFavouriteResponse(
    val message: String,
    val id: Int? = null
)