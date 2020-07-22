package com.tomitive.avia.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.tomitive.avia.R
import com.tomitive.avia.databinding.FragmentActivityAirbaseDataMetarBinding
import com.tomitive.avia.model.airports

private const val ARG_AP_NAME = "param1"
private const val ARG_PARAM2 = "param2"

class ActivityAirbaseDataMetar : Fragment() {
    private var airportName: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            airportName = it.getString(ARG_AP_NAME)
            param2 = it.getString(ARG_PARAM2)
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
        return view
    }

    companion object {
        fun newInstance(airportName: String, param2: String) =
            ActivityAirbaseDataMetar().apply {
                arguments = Bundle().apply {
                    putString(ARG_AP_NAME, airportName)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}