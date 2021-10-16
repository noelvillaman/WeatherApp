package com.noelvillaman.software.weatherapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherAPI {
    private val BASE_URL = "https://api.openweathermap.org/"

    private var retrofit: Retrofit? = null
    private var weatherService: WeatherService? = null

    val instance: WeatherService?
        get() {
            if (weatherService != null) {
                return weatherService!!
            }
            if (retrofit == null) {
                initializeRetrofit()
            }
            weatherService = retrofit?.create(WeatherService::class.java)
            return weatherService
        }

    private fun initializeRetrofit() {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}