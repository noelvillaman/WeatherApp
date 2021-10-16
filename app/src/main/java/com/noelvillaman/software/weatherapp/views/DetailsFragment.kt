package com.noelvillaman.software.weatherapp.views

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.noelvillaman.software.weatherapp.R
import com.noelvillaman.software.weatherapp.viewmodel.SelectedWeatherViewModel
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlin.concurrent.fixedRateTimer

class DetailsFragment : Fragment() {

    @BindView(R.id.tv_weather_temp)
    lateinit var weatherTempTextView: TextView
    @BindView(R.id.tv_weather_feels_like)
    lateinit var weatherFeelsLikeTextView: TextView
    @BindView(R.id.tv_weather)
    lateinit var weatherTextView: TextView
    @BindView(R.id.tv_weather_description)
    lateinit var weatherDescriptionTextView: TextView

    private lateinit var unbinder: Unbinder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        weatherTempTextView = view.findViewById(R.id.tv_weather_temp)
        weatherFeelsLikeTextView = view.findViewById(R.id.tv_weather_feels_like)
        weatherTextView = view.findViewById(R.id.tv_weather)
        weatherDescriptionTextView = view.findViewById(R.id.tv_weather_description)
        unbinder = ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        displayWeatherDetail()
    }

    private fun displayWeatherDetail() {
        val selectedWeatherViewModel = activity?.let {
            ViewModelProvider(it)
                .get(SelectedWeatherViewModel::class.java)
        }
        selectedWeatherViewModel!!.getSelectedWeatherItem().observe(viewLifecycleOwner, Observer { weather ->
            weatherTempTextView.text = weather.main?.temp.toString()
            weatherFeelsLikeTextView.text = "Feels like: ${weather.main?.feel_like.toString()}"
            weatherTextView.text = weather.weatherList?.get(0)?.main
            weatherDescriptionTextView.text = weather.weatherList?.get(0)?.description
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (unbinder != null){
            unbinder.unbind()
        }
    }

}