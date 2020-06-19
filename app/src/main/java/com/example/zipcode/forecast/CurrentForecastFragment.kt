package com.example.zipcode.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.zipcode.*
import com.example.zipcode.api.CurrentWeather
import com.google.android.material.floatingactionbutton.FloatingActionButton as FloatingActionButton1
/**
 * Displays the current forecast for the current saved location
 */
class CurrentForecastFragment : Fragment() {

    private lateinit var locationRepository: LocationRepository
    private val forecastRespository = ForecastRespository()
    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())
        val view = inflater.inflate(R.layout.fragment_current_forecast, container, false)
        val locationName: TextView = view.findViewById(R.id.locationName)
        val tempText: TextView = view.findViewById(R.id.tempText)
        val emptyText = view.findViewById<TextView>(R.id.emptyText)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val zipcode  = arguments?.getString(KEY_ZIPCODE) ?:""

        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())



        val locationEntryButton : FloatingActionButton1 = view.findViewById(R.id.locationEntryButton)
        locationEntryButton.setOnClickListener{
            showLocationEntry()
        }

        val currentWeatherObserver = Observer<CurrentWeather>{ weather->
            emptyText.visibility = View.GONE
            progressBar.visibility = View.GONE
            locationName.visibility = VISIBLE
            tempText.visibility = VISIBLE

            locationName.text = weather.name
            tempText.text = formatTempForDisplay(weather.forecast.temp, tempDisplaySettingManager.getTempDisplaySetting())
        }

        forecastRespository.currentWeather.observe(viewLifecycleOwner, currentWeatherObserver)

        locationRepository = LocationRepository(requireContext())
        val savedLocationObserver = Observer<Location>{savedLocation ->
            when(savedLocation){
                is Location.Zipcode -> {
                    progressBar.visibility = View.VISIBLE
                    forecastRespository.loadCurrentForecast(savedLocation.zipcode)
                }
            }
        }

        locationRepository.savedLocation.observe(viewLifecycleOwner,savedLocationObserver)

        return view
    }



    private  fun showLocationEntry(){
        val action  = CurrentForecastFragmentDirections.actionCurrentForecastFragmentToLocationEntryFragment()
        findNavController().navigate(action)
    }



    companion object{
        const val KEY_ZIPCODE = "key-zipcode"
        fun newInstance(zipcode:String):CurrentForecastFragment{
            val fragment = CurrentForecastFragment()

            val args = Bundle()

            args.putString(KEY_ZIPCODE, zipcode)
            fragment.arguments  = args

            return fragment
        }
    }
}