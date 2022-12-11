package com.adamgibbons.onlyvansv2.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class VanModel(
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var image64: String = "",
    var color: String = "",
    var year: Int = 2022,
    var engine: Double = 2.0,
    var location: Location = Location()
) : Parcelable {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "title" to title,
            "description" to description,
            "image64" to image64,
            "color" to color,
            "year" to year,
            "engine" to engine,
            "location" to location
        )
    }
}

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable