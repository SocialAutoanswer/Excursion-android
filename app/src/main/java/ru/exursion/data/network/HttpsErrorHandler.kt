package ru.exursion.data.network

import retrofit2.Response
import ru.exursion.data.CanNotGetDataException
import ru.exursion.data.ForbiddenException
import ru.exursion.data.InternalServerException
import ru.exursion.data.UnauthorizedException

fun <T> createHttpError(response: Response<T>) = when(response.code()) {
    500 -> InternalServerException()
    401 -> UnauthorizedException()
    403 -> ForbiddenException()
    else -> CanNotGetDataException()
}