package ru.exursion.domain

import android.annotation.SuppressLint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import ru.exursion.data.auth.AuthRepository
import ru.exursion.data.models.User
import io.reactivex.rxjava3.core.Single.error
import ru.exursion.data.network.AuthHeaderInterceptor
import ru.exursion.domain.settings.UserSettings
import javax.inject.Inject

interface AuthUseCase {

    fun signUp(user: User): Single<User>

    fun signIn(user: User): Single<User>

    fun sendAuthCode(): Single<Unit>

    fun confirmAuthCode(code: String): Single<Unit>

}


class AuthUseCaseImpl @Inject constructor(
    private val repository: AuthRepository,
    private val userSettings: UserSettings
): AuthUseCase {

    @SuppressLint("CheckResult")
    override fun signUp(user: User) =
        repository.signUp(user)
            .doAfterSuccess {
                if(it.isFailure) return@doAfterSuccess

                userSettings.fillAllPrefs(it.getOrNull())
                AuthHeaderInterceptor.setSessionToken(it.getOrNull()?.token ?: "")
            }
            .map { result ->
                if (result.isFailure) {
                    error<Exception>(result.exceptionOrNull() ?: Exception())
                }

                result.getOrNull() ?: User()
            }
            .observeOn(AndroidSchedulers.mainThread())

    @SuppressLint("CheckResult")
    override fun signIn(user: User) =
        repository.login(user)
            .doAfterSuccess {
                if(it.isFailure) return@doAfterSuccess
                userSettings.fillAllPrefs(it.getOrNull())
            }
            .map { result ->
                if (result.isFailure) {
                    error<Exception>(result.exceptionOrNull() ?: Exception())
                }

                result.getOrNull() ?: User()
            }
            .observeOn(AndroidSchedulers.mainThread())


    @SuppressLint("CheckResult")
    override fun sendAuthCode() =
        repository.sendAuthCode()
            .map { result ->
                if (result.isFailure) {
                    error<Exception>(result.exceptionOrNull() ?: Exception())
                }
            }
            .observeOn(AndroidSchedulers.mainThread())

    @SuppressLint("CheckResult")
    override fun confirmAuthCode(code: String) =
        repository.confirmAuthCode(code)
            .map { result ->
                if (result.isFailure) {
                    error<Exception>(result.exceptionOrNull() ?: Exception())
                }
            }
            .observeOn(AndroidSchedulers.mainThread())

}