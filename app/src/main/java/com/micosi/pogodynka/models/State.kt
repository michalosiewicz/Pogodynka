package com.micosi.pogodynka.models

sealed class State<T> {

    data class Success<T>(val data: T) : State<T>()
    data class Error<T>(val code: Int) : State<T>()
}
