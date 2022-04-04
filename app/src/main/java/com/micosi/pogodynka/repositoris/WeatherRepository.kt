package com.micosi.pogodynka.repositoris

import com.micosi.pogodynka.api.RetrofitInstance
import com.micosi.pogodynka.constants.Constants.API_KEY
import com.micosi.pogodynka.constants.Constants.API_LANG
import com.micosi.pogodynka.constants.Constants.UNITS
import com.micosi.pogodynka.mappers.ApiWeatherToAppWeatherMapper
import com.micosi.pogodynka.models.AppWeather
import com.micosi.pogodynka.models.State

class WeatherRepository {

    private val apiWeatherToAppWeatherMapper = ApiWeatherToAppWeatherMapper()

    suspend fun getWeather(city: String): State<AppWeather?> {
        return try {
            val response = RetrofitInstance.retrofit.getWeather(city, API_KEY, API_LANG, UNITS)
            if (response.isSuccessful) {
                State.Success(
                    response.body()?.let { body -> apiWeatherToAppWeatherMapper.map(body) })
            } else {
                State.Error(response.code())
            }
        } catch (e: Exception) {
            State.Error(200)
        }
    }
}