package ru.exursion.data.network

import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import ru.bibaboba.kit.retrofit.EndpointUrl
import ru.exursion.BuildConfig
import ru.exursion.data.models.CitiesPageDto
import ru.exursion.data.models.TagPageDto
import ru.exursion.data.models.UserRequestDto

@EndpointUrl(BuildConfig.EXC_URL)
interface ExcursionApi {

    @POST("auth/login")
    @Multipart
    fun login(
        @Part("email") email: String,
        @Part("password") password: String
    ): Single<Response<UserRequestDto>>

    @GET("locations/cities")
    fun requestCitiesPage(
        @Query("page") page: Int
    ): Single<Response<CitiesPageDto>>

    @GET("locations/tags")
    fun requestTags(
        @Query("page") page: Int
    ): Single<Response<TagPageDto>>
}