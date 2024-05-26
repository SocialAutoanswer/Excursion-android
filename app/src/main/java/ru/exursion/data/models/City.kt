package ru.exursion.data.models

import com.google.gson.annotations.SerializedName

data class CitiesPageDto(
    @SerializedName("next") val nextPageLink: String?,
    @SerializedName("previous") val previousPageLink: String?,
    @SerializedName("data") val cities: List<CityDto?>?,
)

data class CityDto(
    @SerializedName("name") val name: String?,
    @SerializedName("photo") val photo: String?,
    @SerializedName("longitude") val longitude: String?,
    @SerializedName("latitude") val latitude: String?
)

data class City(
    val name: String,
    val image: String,
    val longitude: String?,
    val latitude: String?,
)
