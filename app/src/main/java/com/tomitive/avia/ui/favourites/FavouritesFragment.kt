package com.tomitive.avia.ui.favourites

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tomitive.avia.MainActivity
import com.tomitive.avia.R
import com.tomitive.avia.model.Reservation
import com.tomitive.avia.model.reservations
import com.tomitive.avia.utils.MarginItemDecoration
import kotlin.concurrent.thread

class FavouritesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_favourites, container, false)
        val favouritesList = rootView.findViewById<RecyclerView>(R.id.favourites_list)

        val username = activity?.getSharedPreferences(
            getString(R.string.preferencesName),
            Context.MODE_PRIVATE
        )?.getString(getString(R.string.sharedUsername), "default") ?: "empty"

        val myReservations = reservations.filter { it.studentId == username.toLong() }
        with(rootView as SwipeRefreshLayout) {
            setOnRefreshListener {
                thread {
                    (requireContext() as MainActivity).downloadReservations()
                }.join()

                rootView.isRefreshing = false
                (favouritesList.adapter as FavouritesViewAdapter).data =
                    reservations.filter { it.studentId == username.toLong() } as MutableList<Reservation>
                post { favouritesList.adapter?.notifyDataSetChanged() }
            }
        }
        initRecyclerView(favouritesList, myReservations as MutableList<Reservation>)

        return rootView
    }

    private fun initRecyclerView(
        favouritesList: RecyclerView,
        myReservations: MutableList<Reservation>
    ) {
        favouritesList.apply {
            adapter = FavouritesViewAdapter(requireContext(), myReservations)
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