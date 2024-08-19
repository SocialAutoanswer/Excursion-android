package ru.exursion.domain

import androidx.paging.PagingData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import ru.exursion.data.models.Question
import ru.exursion.data.models.User
import ru.exursion.data.profile.ProfileRepository
import ru.exursion.domain.settings.UserSettings
import javax.inject.Inject

interface ProfileUseCase {
    fun getProfile(): Single<User>
    fun editProfile(user: User): Single<Unit>
    fun deleteProfile(): Single<Unit>
    fun getQuestions(): Flowable<PagingData<Question>>
}

class ProfileUseCaseImpl @Inject constructor(
    private val repository: ProfileRepository,
    private val userSettings: UserSettings
): ProfileUseCase {

    override fun getProfile() = repository.getProfile()
        .map { result ->
            result.getOrThrow()
        }
        .observeOn(AndroidSchedulers.mainThread())

    override fun editProfile(user: User) = repository.editProfile(user)
        .doAfterSuccess { userSettings.fillAllUserPrefs(user) }
        .map { result ->
            result.getOrThrow()
        }
        .observeOn(AndroidSchedulers.mainThread())


    override fun deleteProfile() = repository.deleteProfile()
        .map { result ->
            result.getOrThrow()
        }
        .observeOn(AndroidSchedulers.mainThread())

    override fun getQuestions(): Flowable<PagingData<Question>> = repository.getQuestions()
            .observeOn(AndroidSchedulers.mainThread())

}