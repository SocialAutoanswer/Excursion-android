package ru.exursion.data.locations

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.CanNotGetDataException
import ru.exursion.data.models.Hotel
import ru.exursion.data.models.HotelDto
import ru.exursion.data.network.ExcursionApi
import ru.exursion.data.network.createHttpError
import javax.inject.Inject

interface HotelsRepository {
    fun getHotelById(hotelId: Long): Single<Result<Hotel>>
}

class HotelsRepositoryImpl @Inject constructor(
    private val api: ExcursionApi,
    private val hotelsMapper: Mapper<HotelDto, Hotel>
): HotelsRepository {

    override fun getHotelById(hotelId: Long): Single<Result<Hotel>> {
        return api.getHotelById(hotelId)
            .subscribeOn(Schedulers.io())
            .map {
                if (it.isSuccessful) {
                    val dto = it.body() ?: return@map Result.failure(CanNotGetDataException())
                    Result.success(hotelsMapper.map(dto))
                } else {
                    Result.failure(createHttpError(it))
                }
            }
    }

}