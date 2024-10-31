package ru.lostgirl.mymapapp.model

import com.yandex.mapkit.geometry.Point


data class Marker(
    val id: Int,
    val point: Point,
    val description: String,
)