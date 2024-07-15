package ru.exursion.domain

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import ru.exursion.data.models.User
import ru.exursion.data.profile.ProfileRepository
import javax.inject.Inject

interface ProfileUseCase {
    fun getProfile(): Single<Result<User>>
    fun editProfile(user: User): Single<Result<Unit>>
    fun deleteProfile(): Single<Result<Unit>>
}

class ProfileUseCaseImpl @Inject constructor(
    private val repository: ProfileRepository
): ProfileUseCase {

    override fun getProfile() = repository.getProfile()
        .flatMap { result ->
            if (result.isFailure) {
                Single.error(result.exceptionOrNull() ?: Exception())
            } else {
                Single.just(result)
            }
        }
        .observeOn(AndroidSchedulers.mainThread())

    override fun editProfile(user: User) = repository.editProfile(user)
        .flatMap { result ->
            if (result.isFailure) {
                Single.error(result.exceptionOrNull() ?: Exception())
            } else {
                Single.just(result)
            }
        }
        .observeOn(AndroidSchedulers.mainThread())


    override fun deleteProfile() = repository.deleteProfile()
        .flatMap { result ->
            if (result.isFailure) {
                Single.error(result.exceptionOrNull() ?: Exception())
            } else {
                Single.just(result)
            }
        }
        .observeOn(AndroidSchedulers.mainThread())
}