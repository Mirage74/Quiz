package com.balex.quiz.data.api

import com.balex.quiz.domain.entity.CountriesResponse
import com.balex.quiz.domain.entity.UserScoreResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("get")
    fun loadCountries(): Single<CountriesResponse>

    @FormUrlEncoded
    @POST("createUser")
    fun registerUser(
        @Field("login") login: String,
        @Field("password") password: String
    ): Single<String>


    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("login") login: String,
        @Field("password") password: String
    ): Single<UserScoreResponse>

    @FormUrlEncoded
    @POST("userScore")
    fun getUserScore(
        @Field("username") username: String,
    ): Single<UserScoreResponse>


    @FormUrlEncoded
    @POST("updateUser")
    fun updateUser(
        @Field("login") login: String,
        @Field("BESTSCORE") bestScore: String,
        @Field("LAST_RES") lastRes: String
    ): Single<String>

}