package com.tomitive.avia.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tomitive.avia.R
import com.tomitive.avia.model.Airport
import com.tomitive.avia.model.airports
import com.tomitive.avia.utils.MarginItemDecoration

class FavouritesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_favourites, container, false)
        val favouritesList = rootView.findViewById<RecyclerView>(R.id.favourites_list)

        val favAirports = airports.filter { it.isFavourite }

        initRecyclerView(favouritesList, favAirports)

        return rootView
    }

    private fun initRecyclerView(favouritesList: RecyclerView, favAirports: List<Airport>) {
        favouritesList.apply {
            adapter = FavouritesViewAdapter(requireContext(), favAirports)
            addItemDecoration(
                MarginItemDecoration(
                    resources.getDimension(R.dimen.recyclerview_item_padding).toInt()
                )
            )
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)

        }
        //favouritesList.addOnScrollListener(RecyclerViewCenterItemListener())
    }
}