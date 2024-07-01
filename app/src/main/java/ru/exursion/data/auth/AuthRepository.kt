package ru.exursion.data.auth

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.CanNotGetDataException
import ru.exursion.data.IncorrectPassword
import ru.exursion.data.InternalServerError
import ru.exursion.data.models.User
import ru.exursion.data.models.UserRequestDto
import ru.exursion.data.network.ExcursionApi
import javax.inject.Inject

interface AuthRepository {

    fun login(email: String, password: String): Single<Result<User>>

    fun confirmAuthCode(code: String): Single<Result<Unit>>

    fun sendAuthCode(email: String): Single<Result<Unit>>

    fun signUp(user: User): Single<Result<Unit>>
}

class AuthRepositoryImpl @Inject constructor(
    private val api: ExcursionApi,
    private val userMapper: Mapper<UserRequestDto, User>
) : AuthRepository {

    override fun login(email: String, password: String): Single<Result<User>> {
        return api.login(email, password)
            .subscribeOn(Schedulers.io())
            .map {
                if (it.isSuccessful) {
                    val userRequestDto = it.body() ?: return@map Result.failure(CanNotGetDataException())
                    Result.success(userMapper.map(userRequestDto))
                } else {
                    when (it.code()) {
                        500 -> Result.failure(InternalServerError())
                        400 -> Result.failure(IncorrectPassword)
                        else -> Result.failure(CanNotGetDataException())
                    }
                }
            }
            .onErrorReturn { Result.failure(CanNotGetDataException()) }
    }
}