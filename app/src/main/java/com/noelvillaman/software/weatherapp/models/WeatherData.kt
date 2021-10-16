package com.noelvillaman.software.weatherapp.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WeatherData {
    @SerializedName("list")
    @Expose
    var list: List<ObjectList>? = null
}

class ObjectList {
    @SerializedName("main")
    @Expose
    var main: Main? = null

    @SerializedName("weather")
    @Expose
    var weatherList: List<Weather>? = null
}

class Main {
    @SerializedName("temp")
    @Expose
    var temp: Double? = null

    @SerializedName("feels_like")
    @Expose
    var feel_like: Double? = null

    @SerializedName("pressure")
    @Expose
    var pressure: Int? = null
}

class Weather{
    @SerializedName("main")
    @Expose
    var main: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null
}
