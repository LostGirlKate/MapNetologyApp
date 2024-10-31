package ru.lostgirl.mymapapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Marker(
    val id: Int,
    val longitude: Double,
    val latitude: Double,
    val description: String,
) : Parcelable