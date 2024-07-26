package ru.exursion.data.network

import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import ru.bibaboba.kit.retrofit.EndpointUrl
import ru.exursion.BuildConfig
import ru.exursion.data.models.AudioLocationDto
import ru.exursion.data.models.CityDto
import ru.exursion.data.models.EmailConfirmCode
import ru.exursion.data.models.LocationDto
import ru.exursion.data.models.PageDto
import ru.exursion.data.models.QuestionDto
import ru.exursion.data.models.ReviewDto
import ru.exursion.data.models.RouteDetailsDto
import ru.exursion.data.models.RouteRequestDto
import ru.exursion.data.models.TagPageDto
import ru.exursion.data.models.UserDto
import ru.exursion.data.models.UserRequestDto

@EndpointUrl(BuildConfig.EXC_URL)
interface ExcursionApi {

    @POST("auth/login/")
    fun login(
        @Body user: UserDto
    ): Single<Response<UserRequestDto>>


    @POST("auth/register/")
    fun signUp(
        @Body user: UserDto
    ): Single<Response<UserRequestDto>>

    @GET("auth/send_email_confirm_code/")
    fun sendVerificationCode(): Single<Response<Unit>>

    @POST("auth/emailconfirm/")
    fun confirmEmail(
        @Body code: EmailConfirmCode
    ): Single<Response<Unit>>

    @GET("auth/profile/")
    fun getProfile(): Single<Response<UserDto>>

    @PUT("auth/profile/")
    fun editProfile(
        @Body user: UserDto
    ): Single<Response<Unit>>

    @DELETE("auth/profile/")
    fun deleteProfile(): Single<Response<Unit>>

    @GET("questions/")
    fun getQuestions(
        @Query("page") page: Int
    ): Single<Response<PageDto<QuestionDto>>>


    @GET("locations/cities/")
    fun requestCitiesPage(
        @Query("page") page: Int
    ): Single<Response<PageDto<CityDto>>>

    @GET("locations/tags/")
    fun requestTags(
        @Query("page") page: Int
    ): Single<Response<TagPageDto>>

    @GET("locations/routes/bycity/{cityId}")
    fun requestRoutesByCity(
        @Path("cityId") cityId: Long,
        @Query("page") page: Int,
    ): Single<Response<RouteRequestDto>>

    @GET("locations/routes/{routeId}")
    fun requestRouteDetailsById(
        @Path("routeId") routeId: Long,
    ): Single<Response<RouteDetailsDto>>

    @GET("reviews/route/{routeId}")
    fun requestRouteReviews(
        @Path("routeId") routeId: Long,
    ): Single<Response<List<ReviewDto>>>

    @GET("locations/bycity/{cityId}")
    fun requestLocationsByCity(
        @Path("cityId") cityId: Long,
        @Query("page") page: Int
    ): Single<Response<PageDto<LocationDto>>>

    @GET("locations/{id}")
    fun getLocationById(
        @Path("id") locationId: Long
    ): Single<Response<AudioLocationDto>>
}