package com.tomitive.avia.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tomitive.avia.R
import com.tomitive.avia.utils.MarginItemDecoration
import com.tomitive.avia.utils.MetarManager

class FavouritesFragment : Fragment() {


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_favourites, container, false)
        val favouritesList = rootView.findViewById<RecyclerView>(R.id.favourites_list)
        initRecyclerView(favouritesList)
2
        return rootView
    }

    private fun initRecyclerView(favouritesList: RecyclerView){
        favouritesList.adapter = FavouritesViewAdapter(requireContext(), MetarManager().forecast)
        favouritesList.addItemDecoration(
            MarginItemDecoration(resources.getDimension(R.dimen.recyclerview_item_padding).toInt())
        )
        //favouritesList.addOnScrollListener(RecyclerViewCenterItemListener())
        favouritesList.layoutManager = LinearLayoutManager(requireContext())
        favouritesList.setHasFixedSize(true)
    }
}