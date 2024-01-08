package com.balex.quiz.data.api

import com.balex.quiz.data.pojo.CountriesResponse
import com.balex.quiz.data.pojo.UserScore
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("get")
    fun loadCountries(): Single <CountriesResponse>

    @POST("login")
    //fun login(@Body authRequest: AuthRequest): Single<UserScore>
    fun login(@Body authRequest: AuthRequest): Single<UserScore>

}