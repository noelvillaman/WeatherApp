package com.noelvillaman.software.weatherapp.interfaces

import com.noelvillaman.software.weatherapp.models.ObjectList

interface WeatherListener {
        fun onWeatherItemSelected(weatherData: ObjectList)
}