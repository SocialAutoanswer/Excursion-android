package ru.exursion.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ShopDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("is_recomended") val isRecommended: Boolean?,
    @SerializedName("address") val address: String?,
    @SerializedName("city") val cityId: Long?,
)

data class Shop(
    val id: Long,
    val name: String,
    val description: String,
    val isRecommended: Boolean,
    val address: String,
    val cityId: Long,
    val imageUrl: String
) : Serializable