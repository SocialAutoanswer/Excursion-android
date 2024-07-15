package ru.exursion.data.models

import com.google.gson.annotations.SerializedName

data class LocationDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("longitude") val longitude: Double?,
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("city") val cityId: Long?,
    @SerializedName("route") val routes: List<Long?>?,
)

data class Location(
    val id: Long,
    val name: String,
    val description: String,
    val longitude: Double,
    val latitude: Double,
    val cityId: Long,
    val routesIds: List<Long>,
)