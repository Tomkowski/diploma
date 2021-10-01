package com.tomitive.avia.ui.reservations

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

class ReservationsFragment : Fragment() {

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

        with(rootView as SwipeRefreshLayout) {
            setOnRefreshListener {
                thread {
                    (requireContext() as MainActivity).downloadReservations()
                }.join()

                rootView.isRefreshing = false
                (favouritesList.adapter as ReservationsViewAdapter).data =
                    reservations
                post { favouritesList.adapter?.notifyDataSetChanged() }
            }
        }
        initRecyclerView(favouritesList, reservations)

        return rootView
    }

    private fun initRecyclerView(
        favouritesList: RecyclerView,
        myReservations: MutableList<Reservation>
    ) {
        favouritesList.apply {
            adapter = ReservationsViewAdapter(requireContext(), myReservations)
            addItemDecoration(
                MarginItemDecoration(
                    resources.getDimension(R.dimen.recyclerview_item_padding).toInt()
                )
            )
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)

        }
    }
}