package ru.exursion.domain

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import ru.exursion.data.auth.AuthRepository
import ru.exursion.data.models.User
import ru.exursion.data.network.AuthHeaderInterceptor
import ru.exursion.domain.settings.UserSettings
import javax.inject.Inject

interface AuthUseCase {

    fun signUp(user: User): Single<Result<User>>

    fun signIn(user: User): Single<Result<User>>

    fun sendAuthCode(): Single<Result<Unit>>

    fun confirmAuthCode(code: String): Single<Result<Unit>>

}


class AuthUseCaseImpl @Inject constructor(
    private val repository: AuthRepository,
    private val userSettings: UserSettings
): AuthUseCase {

    override fun signUp(user: User) =
        repository.signUp(user)
            .flatMap { result ->
                if (result.isFailure) {
                    Single.error(result.exceptionOrNull() ?: Exception())
                } else {
                    Single.just(result)
                }
            }
            .doAfterSuccess {
                if(it.isFailure) return@doAfterSuccess

                userSettings.fillAllPrefs(it.getOrNull())
                AuthHeaderInterceptor.setSessionToken(it.getOrNull()?.token ?: "")
            }
            .observeOn(AndroidSchedulers.mainThread())

    override fun signIn(user: User) =
        repository.login(user)
            .flatMap { result ->
                if (result.isFailure) {
                    Single.error(result.exceptionOrNull() ?: Exception())
                } else {
                    Single.just(result)
                }
            }
            .doAfterSuccess {
                if(it.isFailure) return@doAfterSuccess
                userSettings.fillAllPrefs(it.getOrNull())
            }
            .observeOn(AndroidSchedulers.mainThread())


    override fun sendAuthCode() =
        repository.sendAuthCode()
            .flatMap { result ->
                if (result.isFailure) {
                    Single.error(result.exceptionOrNull() ?: Exception())
                } else {
                    Single.just(result)
                }
            }
            .observeOn(AndroidSchedulers.mainThread())

    override fun confirmAuthCode(code: String) =
        repository.confirmAuthCode(code)
            .flatMap { result ->
                if (result.isFailure) {
                    Single.error(result.exceptionOrNull() ?: Exception())
                } else {
                    Single.just(result)
                }
            }
            .observeOn(AndroidSchedulers.mainThread())

}