package ru.lostgirl.mymapapp.repository

import ru.lostgirl.mymapapp.model.Marker

interface MarkerRepository {
    fun getAll(): List<Marker>
    suspend fun save(marker: Marker)
    suspend fun delete(id: Int)
}