package com.micosi.pogodynka.mappers

import androidx.lifecycle.MutableLiveData
import com.micosi.pogodynka.api.models.ApiWeather
import com.micosi.pogodynka.models.AppWeather
import java.util.*
import kotlin.math.roundToInt

class ApiWeatherToAppWeatherMapper : Mapper<ApiWeather, AppWeather> {

    override fun map(input: ApiWeather): AppWeather {
        val sunRise = Date(input.sys.sunrise.toLong() * 1000)
        val sunSet = Date(input.sys.sunset.toLong() * 1000)
        return AppWeather(
            MutableLiveData(input.main.temp.roundToInt().toString() + "\u2103"),
            MutableLiveData(input.weather[0].description.replaceFirstChar { firstChar ->
                if (firstChar.isLowerCase()) firstChar.titlecase(
                    Locale.getDefault()
                ) else firstChar.toString()
            }),
            MutableLiveData(input.main.pressure.toString() + " hPa"),
            MutableLiveData(sunRise.hours.toString() + ":" + sunRise.minutes.toString()),
            MutableLiveData(sunSet.hours.toString() + ":" + sunSet.minutes.toString()),
            input.weather[0].icon
        )
    }
}