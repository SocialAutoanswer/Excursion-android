package ru.exursion.domain

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import ru.exursion.data.auth.AuthRepository
import ru.exursion.data.models.User
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

    override fun signUp(user: User) =
        repository.signUp(user)
            .doAfterSuccess {
                if(it.isFailure) return@doAfterSuccess

                userSettings.fillAllUserPrefs(it.getOrNull())
            }
            .map { result ->
                if (result.isFailure) {
                    throw result.exceptionOrNull() ?: Exception()
                }

                result.getOrNull() ?: User()
            }
            .observeOn(AndroidSchedulers.mainThread())

    override fun signIn(user: User) =
        repository.login(user)
            .doAfterSuccess {
                if(it.isFailure) return@doAfterSuccess
                userSettings.fillAllUserPrefs(it.getOrNull())
            }
            .map { result ->
                if (result.isFailure) {
                    throw result.exceptionOrNull() ?: Exception()
                }

                result.getOrNull() ?: User()
            }
            .observeOn(AndroidSchedulers.mainThread())


    override fun sendAuthCode() =
        repository.sendAuthCode()
            .map { result ->
                if (result.isFailure) {
                    throw result.exceptionOrNull() ?: Exception()
                }
            }
            .observeOn(AndroidSchedulers.mainThread())

    override fun confirmAuthCode(code: String) =
        repository.confirmAuthCode(code)
            .map { result ->
                if (result.isFailure) {
                    throw result.exceptionOrNull() ?: Exception()
                }
            }
            .observeOn(AndroidSchedulers.mainThread())

}