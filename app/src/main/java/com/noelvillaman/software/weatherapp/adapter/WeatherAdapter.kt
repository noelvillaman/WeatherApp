package com.noelvillaman.software.weatherapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.noelvillaman.software.weatherapp.R
import com.noelvillaman.software.weatherapp.interfaces.WeatherListener
import com.noelvillaman.software.weatherapp.models.ObjectList
import com.noelvillaman.software.weatherapp.models.WeatherData
import com.noelvillaman.software.weatherapp.viewmodel.WeatherViewmodel

class WeatherAdapter(
    private val listViewModel: WeatherViewmodel,
    lifecycleOwner: LifecycleOwner,
    private val weatherSelectedListener: WeatherListener
) : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    private var weatherSelectedListener1 : WeatherListener? = null
    private val weatherItems: MutableList<ObjectList> = arrayListOf()

    inner class ViewHolder(listItemView: View, weatherSelectedListener: WeatherListener) : RecyclerView.ViewHolder(listItemView) {
        @BindView(R.id.txtv_weather)
        lateinit var weatherTextView: TextView
        @BindView(R.id.txtv_temp)
        lateinit var tempTextView: TextView

        private var weatherData : ObjectList

        init {
            ButterKnife.bind(this, listItemView)
            weatherData = ObjectList()
            weatherTextView = listItemView.findViewById(R.id.txtv_weather)
            tempTextView = listItemView.findViewById(R.id.txtv_temp)

            listItemView.setOnClickListener{
                weatherSelectedListener.onWeatherItemSelected(weatherData)
            }

        }
        fun bind(weatherItem: ObjectList) {
            this.weatherData = weatherItem
            tempTextView.text = "Temp: ${weatherItem.main?.temp}"
            weatherTextView.text = weatherItem.weatherList?.get(0)?.main
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val contex = parent.context
        val inflater = LayoutInflater.from(contex)
        val weatherItemView = inflater.inflate(R.layout.weather_listview, parent, false)
        return ViewHolder(weatherItemView, weatherSelectedListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(weatherItems[position])
    }

    override fun getItemCount(): Int {
        return weatherItems.size
    }

    init {
        this.weatherSelectedListener1 = weatherSelectedListener
        listViewModel.weatherListItems.observe(lifecycleOwner, Observer { weatherData ->
            weatherItems.clear()
            if (weatherData != null) {
                weatherItems.addAll(weatherData)
            }
            notifyDataSetChanged()
        })
        setHasStableIds(true)
    }
}