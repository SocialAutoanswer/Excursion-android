package ru.exursion.data.models

import com.google.gson.annotations.SerializedName

data class HotelDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("url") val imageUrl: String?,
    @SerializedName("rating") val rating: Float?,
    @SerializedName("address") val address: String?,
    @SerializedName("social_media") val socialMedias: List<SocialMediaDto?>?,
    @SerializedName("phone_number") val phoneNumber: String?,
    @SerializedName("is_favorite") val isFavorite: Boolean?
)


data class Hotel(
    val id: Long,
    val name: String,
    val description: String,
    val imageUrl: String,
    val rating: Float,
    val address: String,
    val socialMedias: List<SocialMedia>,
    val phoneNumber: String,
    val isFavorite: Boolean
)