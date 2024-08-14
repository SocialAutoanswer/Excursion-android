package ru.exursion.data.models

import com.google.gson.annotations.SerializedName

data class RouteDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("image") val imageUrl: String?,
    @SerializedName("length") val kilometers: Double?,
    @SerializedName("duration") val duration: Int?,
    @SerializedName("price") val price: Int?,
    @SerializedName("locations") val locations: List<LocationDto?>?
)

data class Route(
    val id: Long,
    val name: String,
    val description: String,
    val imageUrl: String,
    val kilometers: Double,
    val durationInMinutes: Int,
    val price: Int,
    val cityId: Long,
    val isPaid: Boolean
)