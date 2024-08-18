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
import ru.exursion.data.models.EventDto
import ru.exursion.data.models.HotelDto
import ru.exursion.data.models.LocationDto
import ru.exursion.data.models.MessageDto
import ru.exursion.data.models.PageDto
import ru.exursion.data.models.QuestionDto
import ru.exursion.data.models.RecommendationTagsDto
import ru.exursion.data.models.ReviewDto
import ru.exursion.data.models.RouteDetailsDto
import ru.exursion.data.models.RouteDto
import ru.exursion.data.models.TagDto
import ru.exursion.data.models.UserDto
import ru.exursion.data.models.UserRequestDto

@EndpointUrl(BuildConfig.EXC_URL + "/api/")
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
    fun getCitiesPage(
        @Query("page") page: Int
    ): Single<Response<PageDto<CityDto>>>

    @GET("routes/tags/")
    fun getRoutesTags(
        @Query("page") page: Int
    ): Single<Response<PageDto<TagDto>>>

    @GET("routes/")
    fun getRoutes(
        @Query("page") page: Int
    ): Single<Response<PageDto<RouteDto>>>

    @GET("routes/bytag/{id}/")
    fun getRoutesByTag(
        @Path("id") tagId: Long,
        @Query("page") page: Int
    ): Single<Response<PageDto<RouteDto>>>

    @GET("routes/bycity/{id}/")
    fun getRoutesByCity(
        @Path("id") cityId: Long,
        @Query("page") page: Int
    ): Single<Response<PageDto<RouteDto>>>

    @GET("routes/{id}/")
    fun getRouteById(
        @Path("id") routeId: Long
    ): Single<Response<RouteDetailsDto>>

    @GET("routes/favorites/")
    fun getFavoriteRoutes(): Single<Response<List<RouteDto>>>

    @GET("reviews/{routeId}/")
    fun requestRouteReviews(
        @Path("routeId") routeId: Long,
    ): Single<Response<PageDto<ReviewDto>>>

    @POST("reviews/{routeId}")
    fun sendRouteReview(

    )

    @GET("locations")
    fun getLocations(
        @Query("page") pageNum: Int
    ): Single<Response<PageDto<LocationDto>>>

    @GET("locations/bytag/{id}/")
    fun getLocationsByTag(
        @Path("id") tagId: Long,
        @Query("page") pageNum: Int
    ): Single<Response<PageDto<LocationDto>>>

    @GET("locations/bycity/{cityId}/")
    fun getLocationsByCity(
        @Path("cityId") cityId: Long,
        @Query("page") page: Int
    ): Single<Response<PageDto<LocationDto>>>

    @GET("locations/byroute/{id}")
    fun getLocationsByRoute(
        @Path("id") routeId: Long,
        @Query("page") page: Int
    ): Single<Response<PageDto<LocationDto>>>

    @GET("locations/{id}/")
    fun getLocationById(
        @Path("id") locationId: Long
    ): Single<Response<AudioLocationDto>>

    @GET("locations/favorites/")
    fun getFavoriteLocations(): Single<Response<List<LocationDto>>>

    @GET("hotels/favorites/")
    fun getFavoriteHotels(): Single<Response<List<HotelDto>>>

    @GET("events/favorites/")
    fun getFavoriteEvents(): Single<Response<List<EventDto>>>

    @POST("locations/{id}/favorite/")
    fun changeLocationFavoriteState(
        @Path("id") locationId: Long
    ): Single<Response<MessageDto>>

    @POST("routes/{id}/favorite/")
    fun changeRouteFavoriteState(
        @Path("id") routeId: Long
    ): Single<Response<MessageDto>>

    @POST("events/{id}/favorite/")
    fun changeEventFavoriteState(
        @Path("id") eventId: Long
    ): Single<Response<MessageDto>>

    @POST("hotels/{id}/favorite/")
    fun changeHotelFavoriteState(
        @Path("id") hotelId: Long
    ): Single<Response<MessageDto>>

    @GET("auth/recommendations/")
    fun getRecommendationsTags() : Single<Response<RecommendationTagsDto>>

    @GET("locations/unfavoriteall/")
    fun clearAllFavoriteLocations() : Single<Response<Unit>>

    @GET("routes/unfavoriteall/")
    fun clearAllFavoriteRoutes() : Single<Response<Unit>>

    @GET("events/unfavoriteall/")
    fun clearAllFavoriteEvents() : Single<Response<Unit>>

    @GET("hotels/unfavoriteall/")
    fun clearAllFavoriteHotels() : Single<Response<Unit>>

    @GET("events/{id}/")
    fun getEventById(
        @Path("id") eventId: Long
    ): Single<Response<EventDto>>

    @GET("hotels/{id}/")
    fun getHotelById(
        @Path("id") hotelId: Long
    ): Single<Response<HotelDto>>
}