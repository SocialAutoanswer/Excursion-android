package ru.exursion.data.models

import com.google.gson.annotations.SerializedName


data class AudioDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("audio") val audioUrl: String?,
    @SerializedName("location") val locationId: Long?,
    @SerializedName("duration_seconds") val durationInSeconds: Int?
)

data class PhotoDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("photo") val url: String?
)


data class Audio(
    val id: Long,
    val name: String,
    val audioUrl: String,
    val locationId: Long,
    val durationInSeconds: Int
)

data class Photo(
    val id: Long,
    val name: String,
    val url: String
)