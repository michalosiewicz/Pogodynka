package com.micosi.pogodynka.extensions

import androidx.lifecycle.MutableLiveData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun MutableLiveData<String>.setCurrentTime(pattern: String) {
    val current = LocalDateTime.now()

    val formatter = DateTimeFormatter.ofPattern(pattern)
    val formatted = current.format(formatter)
    this.postValue(formatted)
}