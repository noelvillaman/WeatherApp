package com.noelvillaman.software.weatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import butterknife.Unbinder
import com.noelvillaman.software.weatherapp.viewmodel.WeatherViewmodel
import com.noelvillaman.software.weatherapp.views.WeatherDetailActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var unbinder: Unbinder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLookup.setOnClickListener{
            val intent = Intent(this, WeatherDetailActivity::class.java)
            intent.putExtra("cityname", "${editTextCityName.text}".toLowerCase())
            editTextCityName.text.clear()
            startActivity(intent)
        }
    }

}