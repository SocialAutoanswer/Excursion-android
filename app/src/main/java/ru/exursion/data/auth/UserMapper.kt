package ru.exursion.data.auth

import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.models.User
import ru.exursion.data.models.UserRequestDto
import javax.inject.Inject

class UserMapper @Inject constructor(): Mapper<UserRequestDto, User> {
    override fun map(input: UserRequestDto): User {
        TODO("Not yet implemented")
    }
}