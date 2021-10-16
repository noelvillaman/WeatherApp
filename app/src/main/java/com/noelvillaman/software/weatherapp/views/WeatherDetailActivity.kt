package com.noelvillaman.software.weatherapp.views

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.noelvillaman.software.weatherapp.R
import com.noelvillaman.software.weatherapp.adapter.WeatherAdapter
import com.noelvillaman.software.weatherapp.interfaces.WeatherListener
import com.noelvillaman.software.weatherapp.models.ObjectList
import com.noelvillaman.software.weatherapp.models.WeatherData
import com.noelvillaman.software.weatherapp.viewmodel.SelectedWeatherViewModel
import com.noelvillaman.software.weatherapp.viewmodel.WeatherViewmodel
import kotlinx.android.synthetic.main.activity_weather_detail.*
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.tbWeatherListFragment
import kotlinx.android.synthetic.main.fragment_list.tbWeatherListFragmentTitle

class WeatherDetailActivity : AppCompatActivity(), WeatherListener {
    private var viewModel: WeatherViewmodel? = null

    @BindView(R.id.recycler_view)
    internal lateinit var listView: RecyclerView
    @BindView(R.id.tv_error)
    internal lateinit var errorTextView: TextView
    @BindView(R.id.loading_view)
    internal lateinit var loadingView: View

    private lateinit var unbinder: Unbinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_detail)

        listView = findViewById(R.id.recycler_view)
        loadingView = findViewById(R.id.loading_view)
        errorTextView = findViewById(R.id.tv_error)
        unbinder = ButterKnife.bind(this, this)

        viewModel = ViewModelProvider(this).get(WeatherViewmodel::class.java)
        viewModel?.cityName?.value = intent.getStringExtra("cityname")
        listView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
        listView.adapter = WeatherAdapter(viewModel!!, this, this)
        listView.layoutManager = LinearLayoutManager(this)

        with(tbWeatherListFragment){
            background = ColorDrawable(popupTheme)
            val myColor = ContextCompat.getColor(this@WeatherDetailActivity, R.color.white)
            navigationIcon?.setTint(myColor)
        }

        tbWeatherListFragmentTitle.text = viewModel?.cityName?.value?.toUpperCase()
        addPlus("Los Angeles")
        tbWeatherListFragment.setNavigationOnClickListener {
            onBackPressed()
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        val nameObserver = Observer<List<ObjectList>> { list ->
            if (list != null) {
                listView.visibility = View.VISIBLE
            }
        }
        viewModel!!.getWeatherInfoList().observe(this, nameObserver)

        viewModel!!.error.observe(this, Observer<Boolean>{ isError ->
            if (isError) {
                errorTextView.visibility = View.VISIBLE
                listView.visibility = View.GONE
                errorTextView.setText(R.string.api_error_message)
            } else {
                errorTextView.visibility = View.GONE
                errorTextView.text = null
            }
        })
        viewModel!!.getLoading().observe(this, Observer<Boolean>{ isLoading ->

            loadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) {
                errorTextView.visibility = View.GONE
                listView.visibility = View.GONE
            }
        })
    }
    override fun onDestroy() {
        super.onDestroy()
        unbinder.unbind()
    }

    override fun onWeatherItemSelected(weatherData: ObjectList) {
        weather_container.visibility = View.VISIBLE
        val selectedViewModel = let { ViewModelProvider(this).get(SelectedWeatherViewModel::class.java) }
        selectedViewModel.setSelectedWeatherData(weatherData)
        supportFragmentManager.beginTransaction()
            .replace(R.id.weather_container, DetailsFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun addPlus(str : String) : String{
        return str.replace("\\s".toRegex(), "+").toLowerCase()
    }
}