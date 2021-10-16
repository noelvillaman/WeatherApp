package com.noelvillaman.software.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.noelvillaman.software.weatherapp.models.ObjectList
import com.noelvillaman.software.weatherapp.models.WeatherData


class SelectedWeatherViewModel : ViewModel() {
    private val selectedWeatherData = MutableLiveData<ObjectList>()

    fun getSelectedWeatherItem() : LiveData<ObjectList> {
        return selectedWeatherData
    }
    fun setSelectedWeatherData(weatherData: ObjectList) {
        selectedWeatherData.value = weatherData
    }
}