package com.balex.quiz.data.api

import com.balex.quiz.data.pojo.CountriesResponse
import com.balex.quiz.data.pojo.UserScoreResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("get")
    fun loadCountries(): Single <CountriesResponse>

    @FormUrlEncoded
    @POST("login")
    //fun login(@Body authRequest: AuthRequest): Single<UserScore>
    //fun login(@PartMap() partMap: MutableMap<String, RequestBody>): Single<UserScore>
    fun login(@Field("login") login: String, @Field("password") password: String): Single<UserScoreResponse>



}