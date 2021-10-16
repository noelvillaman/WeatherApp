package com.noelvillaman.software.weatherapp.views

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.noelvillaman.software.weatherapp.MainActivity
import com.noelvillaman.software.weatherapp.R
import com.noelvillaman.software.weatherapp.interfaces.WeatherListener
import com.noelvillaman.software.weatherapp.adapter.WeatherAdapter
import com.noelvillaman.software.weatherapp.models.ObjectList
import com.noelvillaman.software.weatherapp.viewmodel.SelectedWeatherViewModel
import com.noelvillaman.software.weatherapp.viewmodel.WeatherViewmodel
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment(), WeatherListener {
    private var unbinder: Unbinder? = null
    private var viewModel: WeatherViewmodel? = null

    @BindView(R.id.recycler_view)
    internal lateinit var listView: RecyclerView
    @BindView(R.id.tv_error)
    internal lateinit var errorTextView: TextView
    @BindView(R.id.loading_view)
    internal lateinit var loadingView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        listView = view.findViewById(R.id.recycler_view)
        loadingView = view.findViewById(R.id.loading_view)
        errorTextView = view.findViewById(R.id.tv_error)
        unbinder = ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(WeatherViewmodel::class.java)
        listView.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
        listView.adapter = WeatherAdapter(viewModel!!, this, this)
        listView.layoutManager = LinearLayoutManager(context)

        with(tbWeatherListFragment){
            background = ColorDrawable(popupTheme)
            val myColor = ContextCompat.getColor(requireContext(), R.color.white)
            navigationIcon?.setTint(myColor)
        }

        tbWeatherListFragmentTitle.text = "Noel Villaman"
        tbWeatherListFragment.setNavigationOnClickListener {
              requireActivity().onBackPressed()
//            requireContext().startActivity(Intent(requireContext(), MainActivity::class.java))
//            requireActivity().finish()
        }
        observeViewModel()
    }

   private fun observeViewModel() {
        val nameObserver = Observer<List<ObjectList>> { list ->
            if (list != null) {
                listView.visibility = View.VISIBLE
            }
        }
        viewModel!!.getWeatherInfoList().observe(viewLifecycleOwner, nameObserver)

        viewModel!!.error.observe(viewLifecycleOwner, Observer<Boolean>{ isError ->
            if (isError) {
                errorTextView.visibility = View.VISIBLE
                listView.visibility = View.GONE
                errorTextView.setText(R.string.api_error_message)
            } else {
                errorTextView.visibility = View.GONE
                errorTextView.text = null
            }
        })
        viewModel!!.getLoading().observe(viewLifecycleOwner, Observer<Boolean>{ isLoading ->

            loadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) {
                errorTextView.visibility = View.GONE
                listView.visibility = View.GONE
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (unbinder != null) {
            unbinder?.unbind()
            unbinder = null
        }
    }

    override fun onWeatherItemSelected(weatherData: ObjectList) {
        val selectedViewModel = activity?.let { ViewModelProvider(it).get(SelectedWeatherViewModel::class.java) }
        selectedViewModel!!.setSelectedWeatherData(weatherData)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.weather_container, DetailsFragment())
            .addToBackStack(null)
            .commit()
    }
}
