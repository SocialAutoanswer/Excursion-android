package ru.exursion.data.models

import com.google.gson.annotations.SerializedName
import com.yandex.mapkit.geometry.Point

data class LocationDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("longitude") val longitude: Double?,
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("city") val cityId: Long?,
    @SerializedName("picture") val imageUrl: String?
)

data class LocationWithCityDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("longitude") val longitude: Double?,
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("city") val city: CityDto?,
)

data class AudioLocationDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("city") val city: CityDto?,
    @SerializedName("audios") val audios: List<AudioDto?>?,
    @SerializedName("photos") val photos: List<PhotoDto?>?,
    @SerializedName("is_favorite") val isFavorite: Boolean?,
    @SerializedName("description") val description: String?,
    @SerializedName("longitude") val longitude: Double?,
    @SerializedName("latitude") val latitude: Double?
)

data class Location(
    val id: Long,
    val name: String,
    val description: String,
    val imageUrl: String,
    val point: Point,
    val cityId: Long
)

data class AudioLocation(
    val id: Long,
    val name: String,
    val description: String,
    val isFavorite: Boolean,
    val city: City?,
    val point: Point,
    val audios: List<Audio>,
    val photos: List<Photo>
)