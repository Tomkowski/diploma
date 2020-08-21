package com.tomitive.avia.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tomitive.avia.R
import com.tomitive.avia.databinding.FragmentActivityAirbaseDataMetarBinding
import com.tomitive.avia.model.Airport
import com.tomitive.avia.model.airports
import com.tomitive.avia.ui.airbasefullinfo.MetarInfoViewAdapter
import com.tomitive.avia.ui.favourites.FavouritesViewAdapter
import com.tomitive.avia.utils.CloudManager
import com.tomitive.avia.utils.MarginItemDecoration
import kotlinx.android.synthetic.main.avia_favourite_item.*

private const val ARG_AP_NAME = "param1"


class ActivityAirbaseDataMetar : Fragment() {
    private var airportName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            airportName = it.getString(ARG_AP_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentActivityAirbaseDataMetarBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_activity_airbase_data_metar,
            container,
            false
        )
        val view = binding.root

        binding.apply {
            rawMetarData = airports.find { it.airportName == airportName }?.rawMetar
        }
        val metarRecyclerView = view.findViewById<RecyclerView>(R.id.metar_info)
        initRecyclerView(metarRecyclerView, prepareData())
        return view
    }

    companion object {
        fun newInstance(airportName: String) =
            ActivityAirbaseDataMetar().apply {
                arguments = Bundle().apply {
                    putString(ARG_AP_NAME, airportName)
                }
            }
    }

    private fun initRecyclerView(metarList: RecyclerView, reports: List<Pair<String, String>>) {
        metarList.apply {
            adapter = MetarInfoViewAdapter(requireContext(), reports)
            addItemDecoration(
                MarginItemDecoration(
                    resources.getDimension(R.dimen.recyclerview_item_padding).toInt()
                )
            )
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)

        }
    }

    private fun prepareData(): List<Pair<String, String>> {
        val airport = airports.find { it.airportName == airportName } ?: return emptyList()
        val metar = airport.metar ?: return emptyList()

        val data = mutableListOf<Pair<String, String>>()
        with(metar) {
            temperature?.run { data.add("Temperature" to "$this °C") }
            dewPoint?.run { data.add("Dew point temperature" to "$this °C") }
            altimeter?.run { data.add("Altimeter" to "$this hPa") }
            wind?.run {
                val direction = if(directionDegrees == null){
                    "Variable wind direction"
                }
                else "$directionDegrees°"

                data.add("Wind" to "$direction at $speed kt")
                if (extreme1 != 0 || extreme2 != 0)
                    data.add("Wind direction varies" to "from ${this.extreme1}° to ${this.extreme2}°")

            }
            clouds?.run {
                val mergedClouds =
                    joinToString("\n") {
                        CloudManager.calculateCloudCoverage(
                            it.quantity,
                            it.height
                        )
                    }
                data.add("Clouds" to if(mergedClouds.isEmpty()) "No significant clouds" else mergedClouds)
            }
            weatherConditions?.run {

                val mergedWeatherCondition =
                    joinToString("\n") { "${it.intensity} ${it.descriptive} " }
                if(!this.isNullOrEmpty()) data.add("Weather condition" to mergedWeatherCondition)
                }
            visibility?.run { data.add("Visibility" to mainVisibility) }
        }
        return data
    }
}