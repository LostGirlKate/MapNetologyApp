package ru.lostgirl.mymapapp.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.lostgirl.mymapapp.model.Marker

@Entity
data class MarkerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val description: String,
    val latitude: Double,
    val longitude: Double,
) {
    fun toDto() = Marker(
        id = id,
        latitude = latitude,
        longitude = longitude,
        description = description
    )

    companion object {
        fun fromDto(dto: Marker) =
            MarkerEntity(
                id = dto.id,
                description = dto.description,
                latitude = dto.latitude,
                longitude = dto.longitude
            )
    }
}
