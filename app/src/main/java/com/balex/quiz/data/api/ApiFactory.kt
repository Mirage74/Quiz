package com.balex.quiz.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory



object ApiFactory {
    private const val BASE_URL = "https://balexvic.com/api/"
    //private const val BASE_URL = "https://10.0.2.2:4000/api/"
    //private const val BASE_URL = "http://localhost:4000/api/"
    private val interceptor = InitInterceptor()

    private fun InitInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL).client(client)
        //.baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}