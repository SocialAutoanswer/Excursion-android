package ru.exursion.data.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.bibaboba.kit.retrofit.buildApi
import ru.exursion.BuildConfig
import ru.exursion.data.models.LocationDto
import ru.exursion.data.models.ReviewDto
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    fun provideGson() : Gson = GsonBuilder()
        .setLenient()
        .registerTypeAdapter(ReviewDto::class.java, ReviewDtoDeserializer())
        .registerTypeAdapter(LocationDto::class.java, LocationDtoDeserializer())
        .create()

    @Provides
    fun provideHttpClient() : OkHttpClient {
        val builder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            builder.addNetworkInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }

        return builder.addNetworkInterceptor(AuthHeaderInterceptor)
            .readTimeout(35, TimeUnit.SECONDS)
            .writeTimeout(35, TimeUnit.SECONDS)
            .connectTimeout(35, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitBuilder(
        client : OkHttpClient,
        gson : Gson
    ) : Retrofit.Builder = Retrofit.Builder()
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())

    @Provides
    fun provideExcursionApi(builder: Retrofit.Builder) = builder.buildApi<ExcursionApi>()
}