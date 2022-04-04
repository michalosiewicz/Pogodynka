package com.micosi.pogodynka.ui.change_city

import androidx.lifecycle.*
import com.micosi.pogodynka.constants.Constants.EMPTY_STRING
import com.micosi.pogodynka.models.State
import com.micosi.pogodynka.repositoris.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChangeCityViewModel() : ViewModel() {

    val city = MutableLiveData(EMPTY_STRING)

    private val weatherRepository = WeatherRepository()

    private val _changeSuccess = MutableLiveData<String>()
    val changeSuccess: LiveData<String>
        get() = _changeSuccess

    private val _changeError = MutableLiveData<String>()
    val changeError: LiveData<String>
        get() = _changeError

    val isCityCorrect = MediatorLiveData<Boolean>().apply {
        addSource(city) { city ->
            this.value = city.isNotEmpty()
        }
    }

    fun changeCity() {
        city.value?.let { city ->
            viewModelScope.launch(Dispatchers.IO) {
                val state = weatherRepository.getWeather(city)
                if (state is State.Success) {
                    _changeSuccess.postValue(city)
                }
                if (state is State.Error) {
                    when (state.code) {
                        404 -> _changeError.postValue("Nie znaleziono podanego miasta.")
                        200 -> _changeError.postValue("Brak połączenia.")
                        else -> _changeError.postValue("Nieznany błąd.")
                    }
                }
            }
        }
    }
}