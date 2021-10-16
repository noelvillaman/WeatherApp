package com.noelvillaman.software.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.noelvillaman.software.weatherapp.models.ObjectList
import com.noelvillaman.software.weatherapp.models.Weather
import com.noelvillaman.software.weatherapp.models.WeatherData
import com.noelvillaman.software.weatherapp.network.WeatherAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherViewmodel : ViewModel() {
    private val keyValue = "65d00499677e59496ca2f318eb68c049"
    val weatherListItems = MutableLiveData<List<ObjectList>>()
    val cityName = MutableLiveData<String>()
    private var weatherCall: Call<WeatherData>? = null
    private var listOfWeather : MutableList<Weather> = arrayListOf()

    val repoLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    init {
        cityName.value = "Garland"
        fetchWeatherInfo(cityName.value ?: "")
    }

    internal fun getWeatherInfoList(): LiveData<List<ObjectList>> {
        return weatherListItems
    }

    internal fun getLoading(): LiveData<Boolean> {
        return loading
    }

    internal fun getCityName() : LiveData<String>{
        return cityName
    }

    internal val error: LiveData<Boolean>
        get() = repoLoadError

    private fun fetchWeatherInfo(cityValue : String) {
        weatherCall = WeatherAPI.instance?.getWeatherData(cityValue, keyValue)
        weatherCall?.enqueue(object : Callback<WeatherData> {
            override fun onResponse(
                call: Call<WeatherData>,
                response: Response<WeatherData>
            ) {
                val weatherList = response.body()?.list
                for (weather in weatherList!!){
                    weather.weatherList?.let { listOfWeather.addAll(it) }
                }
                weatherListItems.value = weatherList ?: arrayListOf()
                loading.value = false
                weatherCall = null
            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                Log.e(javaClass.simpleName, "Error loading weather data", t)
                weatherCall = null
            }
        })
    }

    override fun onCleared() {
        if (weatherCall != null) {
            weatherCall?.cancel()
            weatherCall = null
        }
    }

}