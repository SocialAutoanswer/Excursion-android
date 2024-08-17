package ru.exursion.data.network

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import ru.exursion.data.models.LocationDto
import java.lang.reflect.Type

class LocationDtoDeserializer : JsonDeserializer<LocationDto> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocationDto {
        val jsonObject = json?.asJsonObject

        val city = try {
            jsonObject?.get("city")?.asLong
        } catch (e: Exception) {
            null
        }

        return LocationDto(
            jsonObject?.get("id")?.asLong,
            jsonObject?.get("name")?.asString,
            jsonObject?.get("description")?.asString,
            jsonObject?.get("longitude")?.asDouble,
            jsonObject?.get("latitude")?.asDouble,
            city
        )
    }
}