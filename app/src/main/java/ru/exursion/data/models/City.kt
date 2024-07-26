package ru.exursion.data.models

import com.google.gson.annotations.SerializedName
import com.yandex.mapkit.geometry.Point

data class CityDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("photo") val photo: String?,
    @SerializedName("longitude") val longitude: String?,
    @SerializedName("latitude") val latitude: String?
)

data class City(
    val id: Long,
    val name: String,
    val image: String,
    val point: Point?
)
