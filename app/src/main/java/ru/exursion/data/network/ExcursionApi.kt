package ru.exursion.data.network

import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.bibaboba.kit.retrofit.EndpointUrl
import ru.exursion.BuildConfig
import ru.exursion.data.models.CitiesPageDto

@EndpointUrl(BuildConfig.EXC_URL)
interface ExcursionApi {

    @GET("locations/cities")
    fun requestCitiesPage(
        @Query("page") page: Int
    ): Single<Response<CitiesPageDto>>
}