package com.example.appli_breeds

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.serialization.kotlinx.json.*
import android.util.Log
import com.example.appli_breeds.model.Chien
import com.example.appli_breeds.model.CreateFavouriteRequest
import com.example.appli_breeds.model.CreateFavouriteResponse
import com.example.appli_breeds.model.DogImage
import com.example.appli_breeds.model.Favourite
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class DogRepository {
    val API_KEY = "live_RsTG6pnWLf11WYdRwsQCRgqp4OdwviLwmrcY0ALy7NoMCXa4MHSna4FJ3xCk8V7F"
    val url ="https://api.thedogapi.com/v1"

    val client = HttpClient(CIO) {

        install(ContentNegotiation) {
            json()
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("Ktor-Logger", message)
                }
            }
        }

    }

    // /v1/breeds
    suspend fun getChien(): List<Chien> =
        client.get("$url/breeds") {
            headers {
                append("x-api-key", API_KEY)
            }
        }.body()

    // récupération de l'image associée à la race par son id
    suspend fun dogImage(referenceId: String): DogImage =
        client.get("$url/images/$referenceId") {
            headers { append("x-api-key", API_KEY) }
        }.body()

    suspend fun searchBreeds(query: String): List<Chien> =
        client.get("$url/breeds/search") {
            headers { append("x-api-key", API_KEY) }
            url { parameters.append("q", query) }
        }.body()

    suspend fun getBreedDetails(id: Int): Chien =
        client.get("$url/breeds/$id") {
            headers { append("x-api-key", API_KEY) }
        }.body()

    // --- Favourites
    suspend fun listFavourites(subId: String? = null): List<Favourite> =
        client.get("$url/favourites") {
            headers { append("x-api-key", API_KEY) }
            if (!subId.isNullOrBlank()) url { parameters.append("sub_id", subId) }
        }.body()

    suspend fun createFavourite(imageId: String, subId: String? = null): CreateFavouriteResponse =
        client.post("$url/favourites") {
            headers { append("x-api-key", API_KEY) }
            contentType(ContentType.Application.Json)
            setBody(CreateFavouriteRequest(image_id = imageId, sub_id = subId))
        }.body()

    suspend fun deleteFavourite(favId: Int) {
        client.delete("$url/favourites/$favId") {
            headers { append("x-api-key", API_KEY) }
        }
    }

}