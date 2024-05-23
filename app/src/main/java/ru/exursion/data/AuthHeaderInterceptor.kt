package ru.exursion.data

import okhttp3.Interceptor
import okhttp3.Response

object AuthHeaderInterceptor : Interceptor {

    private var token: String = ""

    fun setSessionToken(token: String){
        this.token = token
    }

    override fun intercept(chain: Interceptor.Chain): Response = with(chain.request().newBuilder()){
        addHeader("Authorization", "Token $token")
        return chain.proceed(build())
    }
}