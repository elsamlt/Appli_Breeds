package com.example.appli_breeds

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.serialization.kotlinx.json.*
import android.util.Log
import com.example.appli_breeds.model.Chien
import com.example.appli_breeds.model.DogImage
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.request

class DogRepository {
    val API_KEY = "live_RsTG6pnWLf11WYdRwsQCRgqp4OdwviLwmrcY0ALy7NoMCXa4MHSna4FJ3xCk8V7F"

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

    // récupération des races de chiens depuis l'API
//    suspend fun getChien() : List<Chien> {
//        val url = "https://api.thedogapi.com/v1/breeds"
//        return client.request(url).body()
//    }

    // /v1/breeds
    suspend fun getChien(): List<Chien> =
        client.get("https://api.thedogapi.com/v1/breeds") {
            headers {
                append("x-api-key", API_KEY)
            }
        }.body()

    // récupération de l'image associée à la race par son id
    suspend fun dogImage(referenceId: String): DogImage =
        client.get("https://api.thedogapi.com/v1/images/$referenceId") {
            headers { append("x-api-key", API_KEY) }
        }.body()

    suspend fun searchBreeds(query: String): List<Chien> =
        client.get("https://api.thedogapi.com/v1/breeds/search") {
            headers { append("x-api-key", API_KEY) }
            url { parameters.append("q", query) }
        }.body()

    suspend fun getBreedDetails(id: Int): Chien =
        client.get("https://api.thedogapi.com/v1/breeds/$id") {
            headers { append("x-api-key", API_KEY) }
        }.body()



}