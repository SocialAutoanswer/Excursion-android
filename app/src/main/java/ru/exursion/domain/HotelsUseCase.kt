package ru.exursion.domain

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import ru.exursion.data.locations.HotelsRepository
import ru.exursion.data.models.Hotel
import javax.inject.Inject

interface HotelsUseCase {
    fun getHotelById(hotelId: Long): Single<Hotel>
}

class HotelsUseCaseImpl @Inject constructor(
    private val hotelsRepository: HotelsRepository
): HotelsUseCase {

    override fun getHotelById(hotelId: Long): Single<Hotel> {
        return hotelsRepository.getHotelById(hotelId)
            .map { result ->
                result.getOrThrow()
            }
            .observeOn(AndroidSchedulers.mainThread())
    }
}