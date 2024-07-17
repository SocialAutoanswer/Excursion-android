package ru.exursion.domain

import androidx.paging.PagingData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.Single.just
import ru.exursion.data.models.Question
import ru.exursion.data.models.User
import ru.exursion.data.profile.ProfileRepository
import ru.exursion.domain.settings.UserSettings
import javax.inject.Inject

interface ProfileUseCase {
    fun getProfile(): Single<Result<User>>
    fun editProfile(user: User): Single<Result<Unit>>
    fun deleteProfile(): Single<Result<Unit>>
    fun getQuestions(): Flowable<PagingData<Question>>
}

class ProfileUseCaseImpl @Inject constructor(
    private val repository: ProfileRepository,
    private val userSettings: UserSettings
): ProfileUseCase {

    override fun getProfile(): Single<Result<User>> = repository.getProfile()
        .flatMap { result ->
            if (result.isFailure) {
                just(Result.success(userSettings.getUser()))
            } else {
                just(result)
            }
        }
        .observeOn(AndroidSchedulers.mainThread())

    override fun editProfile(user: User) = repository.editProfile(user)
        .flatMap { result ->
            if (result.isFailure) {
                Single.error(result.exceptionOrNull() ?: Exception())
            } else {
                just(result)
            }
        }
        .observeOn(AndroidSchedulers.mainThread())


    override fun deleteProfile() = repository.deleteProfile()
        .flatMap { result ->
            if (result.isFailure) {
                Single.error(result.exceptionOrNull() ?: Exception())
            } else {
                just(result)
            }
        }
        .observeOn(AndroidSchedulers.mainThread())

    override fun getQuestions(): Flowable<PagingData<Question>> = repository.getQuestions()
            .observeOn(AndroidSchedulers.mainThread())

}