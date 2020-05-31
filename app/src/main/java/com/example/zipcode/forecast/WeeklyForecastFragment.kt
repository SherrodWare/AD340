package com.example.zipcode.forecast

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zipcode.*

import com.example.zipcode.details.ForecastDetailsFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton as FloatingActionButton1
/**
 * A simple [Fragment] subclass.
 */
class WeeklyForecastFragment : Fragment() {

    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager
    private val forecastRepository = ForecastRepository()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())

        val zipcode = arguments?.getString(KEY_ZIPCODE) ?: ""
        val view = inflater.inflate(R.layout.fragment_weekly_forecast, container, false)


        val locationEntryButton: FloatingActionButton1 = view.findViewById(R.id.locationEntryButton)
        locationEntryButton.setOnClickListener{
           showLocationEntry()

        }

        val dailyForecastList: RecyclerView = view.findViewById(R.id.forecastList)
        dailyForecastList.layoutManager = LinearLayoutManager(requireContext())
        val dailyForecastAdapter = DailyForecastAdapter(tempDisplaySettingManager) { it:DailyForecast ->
            showForecastDetails(it)
        }
        dailyForecastList.adapter = dailyForecastAdapter

        val weeklyForecastObserver = Observer<List<DailyForecast>>{ forecastItems ->
            dailyForecastAdapter.submitList(forecastItems)
        }
        forecastRepository.weeklyForecast.observe(this, weeklyForecastObserver )

        forecastRepository.loadForecast(zipcode)
        return view
    }


    private fun showLocationEntry(){
        val action = WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToLocationEntryFragment()
        findNavController().navigate(action)
    }

    private fun  showForecastDetails(forecast: DailyForecast){
        val action = WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToForecastDetailsFragment2(forecast.temp, forecast.description)
        findNavController().navigate(action)
    }

     companion object {
         const val KEY_ZIPCODE = "key_zipcode"

         fun newInstance(zipcode: String) : WeeklyForecastFragment{
             val fragment = WeeklyForecastFragment()

             val args = Bundle()
             args.putString(KEY_ZIPCODE, zipcode)
             fragment.arguments = args

             return fragment
         }
     }

}
