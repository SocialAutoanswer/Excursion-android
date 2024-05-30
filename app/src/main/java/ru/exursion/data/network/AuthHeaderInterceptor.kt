package ru.exursion.data.network

import okhttp3.Interceptor
import okhttp3.Response
import ru.exursion.BuildConfig

object AuthHeaderInterceptor : Interceptor {

    private var token: String = ""

    fun setSessionToken(token: String){
        AuthHeaderInterceptor.token = token
    }

    override fun intercept(chain: Interceptor.Chain): Response = with(chain.request().newBuilder()){
        addHeader("Authorization", "Token $token")
        return chain.proceed(build())
    }
}