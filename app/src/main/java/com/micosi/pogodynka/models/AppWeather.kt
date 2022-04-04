package com.micosi.pogodynka.models

import androidx.lifecycle.MutableLiveData

data class AppWeather(
    val degrees: MutableLiveData<String>,
    val description: MutableLiveData<String>,
    val pressure: MutableLiveData<String>,
    val east: MutableLiveData<String>,
    val west: MutableLiveData<String>,
    val icon: String
)