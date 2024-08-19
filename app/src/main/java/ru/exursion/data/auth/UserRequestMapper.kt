package ru.exursion.data.auth

import org.threeten.bp.LocalDate
import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.models.User
import ru.exursion.data.models.UserDto
import ru.exursion.data.models.UserRequestDto
import javax.inject.Inject

class UserRequestMapper @Inject constructor(): Mapper<UserRequestDto, User> {
    override fun map(input: UserRequestDto): User = User(
        input.user?.firstName ?: "",
        input.user?.lastName ?: "",
        input.user?.birthDate?.let { LocalDate.parse(it) },
        input.user?.email ?: "",
        input.token ?: "",
        "", //TODO:not getting it from server,
        input.user?.password ?: "",
        input.user?.emailIsVerified ?: false
    )
}

class UserMapper @Inject constructor(): Mapper<UserDto, User> {

    override fun map(input: UserDto): User = User(
        input.firstName ?: "",
        input.lastName ?: "",
        input.birthDate?.let { LocalDate.parse(it) },
        input.email ?: "",
        null,
        null,
        null,
        null
    )

    override fun reverseMap(input: User): UserDto = UserDto(
        null, input.password, null, null, null, null, null,
        input.firstName,
        input.lastName,
        null,
        input.email,
        null, null,
        "${input.birthDate?.year}-${input.birthDate?.monthValue}-${input.birthDate?.dayOfMonth}"
        , null, null
    )

}