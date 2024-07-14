package ru.exursion.data.auth

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.CanNotGetDataException
import ru.exursion.data.EmailAlreadyRegistered
import ru.exursion.data.IncorrectCode
import ru.exursion.data.IncorrectPassword
import ru.exursion.data.InternalServerError
import ru.exursion.data.models.User
import ru.exursion.data.models.UserRequestDto
import ru.exursion.data.network.ExcursionApi
import javax.inject.Inject

interface AuthRepository {

    fun login(user: User): Single<Result<User>>

    fun confirmAuthCode(code: String): Single<Result<Unit>>

    fun sendAuthCode(): Single<Result<Unit>>

    fun signUp(user: User): Single<Result<User>>
}

class AuthRepositoryImpl @Inject constructor(
    private val api: ExcursionApi,
    private val userMapper: Mapper<UserRequestDto, User>
) : AuthRepository {

    override fun login(user: User): Single<Result<User>> {
        return api.login(user.toDto())
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

    override fun signUp(user: User): Single<Result<User>> {
        return api.signUp(user.toDto())
            .subscribeOn(Schedulers.io())
            .map {
                if (it.isSuccessful) {
                    val userRequestDto = it.body() ?: return@map Result.failure(CanNotGetDataException())
                    Result.success(userMapper.map(userRequestDto))
                } else {
                    when (it.code()) {
                        500 -> Result.failure(InternalServerError())
                        400 -> Result.failure(EmailAlreadyRegistered)
                        else -> Result.failure(CanNotGetDataException())
                    }
                }
            }
            .onErrorReturn { Result.failure(CanNotGetDataException()) }
    }

    override fun sendAuthCode(): Single<Result<Unit>> {
        return api.sendVerificationCode()
            .subscribeOn(Schedulers.io())
            .map {
                if (it.isSuccessful) {
                    Result.success(Unit)
                } else {
                    when (it.code()) {
                        500 -> Result.failure(InternalServerError())
                        else -> Result.failure(CanNotGetDataException())
                    }
                }
            }
            .onErrorReturn { Result.failure(CanNotGetDataException()) }
    }

    override fun confirmAuthCode(code: String): Single<Result<Unit>> {
        return api.confirmEmail(code)
            .subscribeOn(Schedulers.io())
            .map {
                if (it.isSuccessful) {
                    Result.success(Unit)
                } else {
                    when (it.code()) {
                        500 -> Result.failure(InternalServerError())
                        400 -> Result.failure(IncorrectCode)
                        else -> Result.failure(CanNotGetDataException())
                    }
                }
            }
            .onErrorReturn { Result.failure(CanNotGetDataException()) }
    }
}