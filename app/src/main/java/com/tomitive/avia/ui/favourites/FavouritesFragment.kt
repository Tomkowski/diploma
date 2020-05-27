package com.tomitive.avia.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tomitive.avia.R
import com.tomitive.avia.utils.MarginItemDecoration
import com.tomitive.avia.utils.MetarManager
import io.github.mivek.model.Metar

class FavouritesFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_favourites, container, false)
        val favouritesList = rootView.findViewById<RecyclerView>(R.id.favourites_list)

        val metarForecast = MetarManager().forecast

        if (metarForecast.isEmpty()) {
            Toast.makeText(
                context,
                getString(R.string.metar_fail_msg),
                Toast.LENGTH_LONG
            ).show()
        }

        initRecyclerView(favouritesList, metarForecast)


        return rootView
    }

    private fun initRecyclerView(favouritesList: RecyclerView, metarForecast: List<Metar>) {
        favouritesList.adapter = FavouritesViewAdapter(requireContext(), metarForecast)
        favouritesList.addItemDecoration(
            MarginItemDecoration(resources.getDimension(R.dimen.recyclerview_item_padding).toInt())
        )
//        favouritesList.addOnScrollListener(RecyclerViewCenterItemListener())
        favouritesList.layoutManager = LinearLayoutManager(requireContext())
        favouritesList.setHasFixedSize(true)


    }
}