package com.micosi.pogodynka.ui.weather_old

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.micosi.pogodynka.constants.Constants.EMPTY_STRING
import com.micosi.pogodynka.extensions.setCurrentTime
import com.micosi.pogodynka.models.AppWeather
import com.micosi.pogodynka.models.State
import com.micosi.pogodynka.repositoris.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OldWeatherViewModel(private val searchedCity: String) : ViewModel() {

    val city = MutableLiveData(searchedCity)
    val date = MutableLiveData(EMPTY_STRING)
    val time = MutableLiveData(EMPTY_STRING)
    val isFragmentOpen = MutableLiveData(true)

    val appWeather = MutableLiveData(
        AppWeather(
            MutableLiveData(EMPTY_STRING),
            MutableLiveData(EMPTY_STRING),
            MutableLiveData(EMPTY_STRING),
            MutableLiveData(EMPTY_STRING),
            MutableLiveData(EMPTY_STRING),
            EMPTY_STRING
        )
    )

    private val weatherRepository = WeatherRepository()

    private val _icon = MutableLiveData<String>()
    val icon: LiveData<String>
        get() = _icon

    private val _changeError = MutableLiveData<String>()
    val changeError: LiveData<String>
        get() = _changeError

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentTime()
        }
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentWeather()
        }
    }

    private suspend fun getCurrentTime() {
        while (isFragmentOpen.value == true) {
            date.setCurrentTime("yyyy-MM-dd")
            time.setCurrentTime("HH:mm")
            delay(1000)
        }
    }

    private suspend fun getCurrentWeather() {
        while (isFragmentOpen.value == true) {
            val state = weatherRepository.getWeather(searchedCity)
            if (state is State.Success) {
                if (state.data != null) {
                    appWeather.postValue(state.data)
                    _icon.postValue(state.data.icon + ".png")
                } else {
                    _changeError.postValue("Brak połączenia. Dane nieaktualne.")
                }
            }
            if (state is State.Error) {
                _changeError.postValue("Brak połączenia. Dane nieaktualne.")
            }
            delay(60000)
        }
    }
}