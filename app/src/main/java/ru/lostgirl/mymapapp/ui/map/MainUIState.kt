package ru.lostgirl.mymapapp.ui.map

import ru.lostgirl.mymapapp.model.Marker

data class MainUIState(
    val markerList: List<Marker> = listOf()
)
