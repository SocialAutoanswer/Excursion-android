package ru.exursion.data.network

import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query
import ru.bibaboba.kit.retrofit.EndpointUrl
import ru.exursion.BuildConfig
import ru.exursion.data.models.CitiesPageDto
import ru.exursion.data.models.DetailDto
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

    @GET("auth/send_confirm_code/")
    fun sendVerificationCode(): Single<Response<DetailDto>>

    @POST("auth/emailconfirm/")
    @Multipart
    fun confirmEmail(
        @Part("email_confirm_code") code: String
    ): Single<Response<DetailDto>>

    @GET("auth/profile/")
    fun getProfile(): Single<Response<UserRequestDto>>

    @PUT("auth/profile/")
    fun editProfile(
        @Body user: UserDto
    ): Single<Response<DetailDto>>

    @DELETE("auth/profile/")
    fun deleteProfile(): Single<Response<DetailDto>>


    @GET("locations/cities/")
    fun requestCitiesPage(
        @Query("page") page: Int
    ): Single<Response<CitiesPageDto>>

    @GET("locations/tags/")
    fun requestTags(
        @Query("page") page: Int
    ): Single<Response<TagPageDto>>
}