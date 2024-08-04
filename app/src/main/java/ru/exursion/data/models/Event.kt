package ru.exursion.data.models

import com.google.gson.annotations.SerializedName

data class EventDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("start_date") val date: String?,
    @SerializedName("address") val address: String?,
    @SerializedName("image") val imageUrl: String?
)

data class Event(
    val id: Long,
    val name: String,
    val date: String,
    val address: String,
    val imageUrl: String
)