package com.micosi.pogodynka.api.services

import com.micosi.pogodynka.api.models.ApiWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("/data/2.5/weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("APPID") key: String,
        @Query("lang") lang: String,
        @Query("units") units: String
    ): Response<ApiWeather>
}