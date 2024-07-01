package ru.exursion.domain

import io.reactivex.rxjava3.core.Single
import ru.exursion.data.auth.AuthRepository
import ru.exursion.data.models.User
import javax.inject.Inject

interface AuthUseCase {

    fun signUp(): Single<Unit>

    fun signIn(): Single<User>

    fun sendAuthCode(email: String): Single<Unit>

    fun confirmAuthCode(code: String): Single<Unit>

}


class AuthUseCaseImpl @Inject constructor(
    private val repository: AuthRepository
): AuthUseCase {

}