package ru.exursion.data.network

import com.google.gson.*
import ru.exursion.data.models.ReviewDto
import ru.exursion.data.models.UserDto
import java.lang.reflect.Type

class UserDtoDeserializer : JsonDeserializer<ReviewDto?> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ReviewDto {
        val jsonObject = json?.asJsonObject

        val user = try {
            Gson().fromJson(jsonObject?.get("user"), UserDto::class.java)
        } catch (e: IllegalStateException) {
            UserDto(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null)
        } catch (e: JsonSyntaxException) {
            UserDto(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null)
        } catch (e: Exception) {
            UserDto(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null)
        }

        return ReviewDto(
            jsonObject?.get("id")?.asLong,
            jsonObject?.get("review")?.asString,
            jsonObject?.get("created_at")?.asString,
            jsonObject?.get("raiting")?.asInt,
            user
        )
    }
}