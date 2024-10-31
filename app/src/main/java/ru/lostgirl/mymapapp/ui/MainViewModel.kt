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
       // inittext()
        loadMarks()
    }

    private fun inittext() {
        save("test 1", 0, Point(59.936046, 30.326869))
        save("test 2", 0, Point(59.938185, 30.32808))
        save("test 3", 0, Point(59.937376, 30.33621))
    }
    private fun loadMarks() = viewModelScope.launch {
        val marks = repository.getAll()
        _state.value = MainUIState(marks)
    }

    fun removeById(id: Int) = viewModelScope.launch {
        repository.delete(id)
    }

    fun save(description: String, id: Int = 0, point: Point) = viewModelScope.launch {
        val marker = Marker(id, point, description)
        repository.save(marker)
    }
}