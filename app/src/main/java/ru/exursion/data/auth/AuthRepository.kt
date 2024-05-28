package ru.exursion.data.auth

import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import ru.exursion.data.models.User
import javax.inject.Inject

interface AuthRepository {

    fun login(email: String, password: String): Single<Response<User>>
}

class AuthRepositoryImpl @Inject constructor(): AuthRepository {

    override fun login(email: String, password: String): Single<Response<User>> {
        TODO("Not yet implemented")
    }
}