package com.tomitive.avia.ui.search

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tomitive.avia.R
import com.tomitive.avia.utils.airportLocation
import com.tomitive.avia.utils.airportName
import kotlinx.android.synthetic.main.avia_search_result.view.*

class SearchViewAdapter(private val context: Context, private var searchItems : List<String> = airportName.map { it.key } ) :
    RecyclerView.Adapter<SearchViewAdapter.SearchView>() {

    class SearchView(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val parentLayout = itemView.findViewById<ConstraintLayout>(R.id.avia_search_result)

        fun setAirportName(name: String) {
            itemView.search_result_airport_name.text = name
        }

        fun setAirportFullName(fullName: String){
            itemView.search_result_airport_full_name.text = fullName
        }

        fun setAirportLocation(location: String){
            itemView.search_result_airport_location.text = location
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchView {
        val from = LayoutInflater.from(context)
        val view = from.inflate(R.layout.avia_search_result, parent, false)

        return SearchView(view)
    }

    override fun getItemCount(): Int {
        Log.d("searchview", "number of items : ${searchItems.size}")
        return searchItems.size
    }

    override fun onBindViewHolder(holder: SearchView, position: Int) {
        val entry = searchItems[position]
        holder.setAirportName(entry)
        airportName[entry]?.run{ holder.setAirportFullName(this)}
        airportLocation[entry]?.run { holder.setAirportLocation(this) }
    }

    fun updateSearchResults(results : List<String>){
        searchItems = results
        Log.d("searchview", "notified! searchItems size is: ${searchItems.size}")
        notifyDataSetChanged()
    }

}