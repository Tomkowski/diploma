package com.tomitive.avia.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.tomitive.avia.R
import com.tomitive.avia.databinding.FragmentActivityAirbaseDataMetarBinding
import com.tomitive.avia.databinding.FragmentActivityAirbaseDataNotamBinding
import com.tomitive.avia.model.Notam
import com.tomitive.avia.model.airports
import com.tomitive.avia.ui.airbasefullinfo.NotamInfoViewAdapter
import com.tomitive.avia.utils.MarginItemDecoration

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

        val binding: FragmentActivityAirbaseDataNotamBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_activity_airbase_data_notam,
            container,
            false
        )
        val view = binding.root

        val notamRecyclerView = view.findViewById<RecyclerView>(R.id.notam_recycler_view)

        val airportNotams = airports.find { it.airportName == airportName }?.notams ?: emptyList()
        initRecyclerView(notamRecyclerView, airportNotams)

        //Log.d("notamFragment", airports.filter { it.isFavourite }.map { it.notams.toString() }.joinToString("\n") )

        return view
    }

    private fun initRecyclerView(recyclerView: RecyclerView, notams: List<Notam>){
        recyclerView.apply{
            adapter = NotamInfoViewAdapter(requireContext(), notams)
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                MarginItemDecoration(
                    resources.getDimension(R.dimen.recyclerview_item_padding).toInt()
                )
            )
            setHasFixedSize(true)
        }
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
