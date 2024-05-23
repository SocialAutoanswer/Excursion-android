package ru.exursion.data.locations

import io.reactivex.rxjava3.core.Single
import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.ExcursionApi
import ru.exursion.data.locations.models.City
import ru.exursion.data.locations.models.CityDto
import javax.inject.Inject

interface LocationsRepository {

    fun getCityPage(page: Int): Single<List<City>>
}

class LocationsRepositoryImpl @Inject constructor(
    private val api: ExcursionApi,
    private val citiesMapper: Mapper<CityDto, City>,
) : LocationsRepository {

    override fun getCityPage(page: Int): Single<List<City>> {
        return api.requestCitiesPage(page).map {
            mutableListOf<City>().apply {
                it.cities?.forEach {
                    it?.let { add(citiesMapper.map(it)) }
                }
            }
        }
    }
}