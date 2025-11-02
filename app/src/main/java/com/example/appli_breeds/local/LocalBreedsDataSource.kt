package com.example.appli_breeds.local

import com.example.appli_breeds.model.Chien
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class LocalBreedsDataSource(private val dao: BreedDao) {
    val allBreedsFlow: Flow<List<Chien>> = dao.getAllFlow().map { it.map(BreedEntityMapper::toDomain) }


    fun searchByNameFlow(q: String): Flow<List<Chien>> =
        dao.searchByNameFlow(q).map { it.map(BreedEntityMapper::toDomain) }


    suspend fun replaceAll(list: List<Chien>) {
        dao.clearAll()
        dao.upsertAll(list.map(BreedEntityMapper::fromDomain))
    }
}

object BreedEntityMapper {
    fun fromDomain(b: Chien) = BreedEntity(
        id = b.id,
        name = b.name,
        breedGroup = b.breed_group.ifBlank { null },
        origin = b.origin.ifBlank { null },
        lifeSpan = b.life_span.ifBlank { null },
        temperament = b.temperament.ifBlank { null },
        heightMetric = b.height.metric.ifBlank { null },
        weightMetric = b.weight.metric.ifBlank { null },
        referenceImageId = b.reference_image_id,
        imageUrl = b.imageUrl
    )


    fun toDomain(e: BreedEntity) = Chien(
        id = e.id,
        name = e.name,
        breed_group = e.breedGroup ?: "",
        origin = e.origin ?: "",
        life_span = e.lifeSpan ?: "",
        temperament = e.temperament ?: "",
        height = com.example.appli_breeds.model.Height(metric = e.heightMetric ?: ""),
        weight = com.example.appli_breeds.model.Weight(metric = e.weightMetric ?: ""),
        reference_image_id = e.referenceImageId ?: "",
        image = com.example.appli_breeds.model.DogImage(id = e.referenceImageId ?: "", url = e.imageUrl ?: "")
    )
}