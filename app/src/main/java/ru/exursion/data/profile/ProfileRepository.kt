package ru.exursion.data.profile

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.CanNotGetDataException
import ru.exursion.data.InternalServerException
import ru.exursion.data.InvalidToken
import ru.exursion.data.ProfileNotVerified
import ru.exursion.data.auth.toDto
import ru.exursion.data.models.User
import ru.exursion.data.models.UserRequestDto
import ru.exursion.data.network.ExcursionApi
import javax.inject.Inject

interface ProfileRepository {
    fun getProfile(): Single<Result<User>>
    fun editProfile(user: User): Single<Result<Unit>>
    fun deleteProfile(): Single<Result<Unit>>
}

class ProfileRepositoryImpl @Inject constructor(
    private val api: ExcursionApi,
    private val userMapper: Mapper<UserRequestDto, User>
): ProfileRepository {

    override fun getProfile(): Single<Result<User>> {
        return api.getProfile()
            .subscribeOn(Schedulers.io())
            .map {
                if (it.isSuccessful) {
                    val userRequestDto = it.body() ?: return@map Result.failure(CanNotGetDataException())
                    Result.success(userMapper.map(userRequestDto))
                } else {
                    when (it.code()) {
                        500 -> Result.failure(InternalServerException())
                        403 -> Result.failure(ProfileNotVerified)
                        401 -> Result.failure(InvalidToken)
                        else -> Result.failure(CanNotGetDataException())
                    }
                }
            }
            .onErrorReturn { Result.failure(CanNotGetDataException()) }
    }

    override fun editProfile(user: User): Single<Result<Unit>> {
        return api.editProfile(user.toDto())
            .subscribeOn(Schedulers.io())
            .map {
                if (it.isSuccessful) {
                    Result.success(Unit)
                } else {
                    when (it.code()) {
                        500 -> Result.failure(InternalServerException())
                        403 -> Result.failure(ProfileNotVerified)
                        else -> Result.failure(CanNotGetDataException())
                    }
                }
            }
            .onErrorReturn { Result.failure(CanNotGetDataException()) }
    }

    override fun deleteProfile(): Single<Result<Unit>> {
        return api.deleteProfile()
            .subscribeOn(Schedulers.io())
            .map {
                if (it.isSuccessful) {
                    Result.success(Unit)
                } else {
                    when (it.code()) {
                        500 -> Result.failure(InternalServerException())
                        403 -> Result.failure(ProfileNotVerified)
                        else -> Result.failure(CanNotGetDataException())
                    }
                }
            }
            .onErrorReturn { Result.failure(CanNotGetDataException()) }
    }
}