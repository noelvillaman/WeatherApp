package com.noelvillaman.software.weatherapp.network

import com.noelvillaman.software.weatherapp.models.WeatherData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
        @GET("data/2.5/forecast")
        fun getWeatherData(@Query("q") city : String, @Query("appid")
        keyvalue : String) : Call<WeatherData>
}
