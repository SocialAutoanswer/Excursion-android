package ru.exursion.data.models

import com.google.gson.annotations.SerializedName


data class AudioDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("audio") val audioUrl: String?,
    @SerializedName("location") val locationId: Long?
)

data class PhotoDto(
    @SerializedName("id") val id: Long?
)


data class Audio(
    val id: Long,
    val name: String,
    val audioUrl: String,
    val locationId: Long
)

data class Photo(
    val id: Long
)