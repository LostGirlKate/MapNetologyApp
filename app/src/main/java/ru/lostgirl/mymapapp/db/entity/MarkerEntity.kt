package ru.lostgirl.mymapapp.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yandex.mapkit.geometry.Point
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
        point = Point(latitude, longitude),
        description = description
    )

    companion object {
        fun fromDto(dto: Marker) =
            MarkerEntity(
                id = dto.id,
                description = dto.description,
                latitude = dto.point.latitude,
                longitude = dto.point.longitude
            )

    }
}
