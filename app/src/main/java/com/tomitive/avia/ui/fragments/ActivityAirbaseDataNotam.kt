package com.tomitive.avia.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tomitive.avia.R
import com.tomitive.avia.model.airports

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

class ActivityAirbaseDataNotam : Fragment() {
    // TODO: Rename and change types of parameters
    private var airportName: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            airportName = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("notamFragment", airports.filter { it.isFavourite }.map { it.notams.toString() }.joinToString("\n") )
        return inflater.inflate(R.layout.fragment_activity_airbase_data_notam, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            ActivityAirbaseDataNotam().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}
