package ru.lostgirl.mymapapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yandex.mapkit.geometry.Point
import kotlinx.coroutines.launch
import ru.lostgirl.mymapapp.db.AppDb
import ru.lostgirl.mymapapp.model.Marker
import ru.lostgirl.mymapapp.repository.MarkerRepository
import ru.lostgirl.mymapapp.repository.MarkerRepositoryImpl
import ru.lostgirl.mymapapp.ui.map.MainUIState

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MarkerRepository = MarkerRepositoryImpl(
        AppDb.getInstance(application).markerDao()
    )

    private val _state = MutableLiveData<MainUIState>()
    val state: LiveData<MainUIState>
        get() = _state

    init {
        loadMarks()
    }

    private fun loadMarks() = viewModelScope.launch {
        val marks = repository.getAll()
        _state.value = MainUIState(marks)
    }

    fun removeById(id: Int) = viewModelScope.launch {
        repository.delete(id)
        loadMarks()
    }

    fun save(description: String, point: Point, id: Int = 0) = viewModelScope.launch {
        val marker = Marker(id, longitude = point.longitude, latitude = point.latitude, description)
        repository.save(marker)
        loadMarks()
    }
}