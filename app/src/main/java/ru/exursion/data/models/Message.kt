package ru.exursion.data.models

import com.google.gson.annotations.SerializedName
import ru.bibaboba.kit.util.Mapper
import javax.inject.Inject

data class MessageDto(
    @SerializedName("message") val content: String?
)

data class Message(
    val content: String
)

class MessageMapper @Inject constructor(): Mapper<MessageDto, Message> {
    override fun map(input: MessageDto): Message = Message(
        input.content ?: ""
    )
}