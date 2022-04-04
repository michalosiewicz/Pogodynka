package com.micosi.pogodynka.api.models

data class WeatherDescription(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)