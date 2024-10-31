package ru.lostgirl.mymapapp.repository

import ru.lostgirl.mymapapp.db.MarkerDao
import ru.lostgirl.mymapapp.db.entity.MarkerEntity
import ru.lostgirl.mymapapp.model.Marker

class MarkerRepositoryImpl(
    private val dao: MarkerDao,
) : MarkerRepository {
    override fun getAll(): List<Marker> = dao.getAll().map { marker ->
        marker.toDto()
    }

    override suspend fun save(marker: Marker) {
        dao.save(MarkerEntity.fromDto(marker))
    }

    override suspend fun delete(id: Int) {
        dao.removeById(id)
    }
}