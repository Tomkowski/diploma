package com.tomitive.avia.ui.search

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tomitive.avia.R
import com.tomitive.avia.model.Reservation
import com.tomitive.avia.model.classrooms
import com.tomitive.avia.model.reservations
import com.tomitive.avia.ui.reservation.ReservationSchedule
import com.tomitive.avia.utils.dateFormattedHHmm
import kotlinx.android.synthetic.main.avia_search_result.view.*
import java.util.*

class SearchViewAdapter(
    private val context: Context,
    private var searchItems: List<String> = classrooms
) :
    RecyclerView.Adapter<SearchViewAdapter.SearchView>() {

    class SearchView(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val parentLayout = itemView.findViewById<ConstraintLayout>(R.id.avia_search_result)

        fun setClassNumber(name: String) {
            itemView.search_result_classroom_number.text = name
        }

        fun setReservationTitle(fullName: String) {
            itemView.search_result_current_reservation_title.text = fullName
        }

        fun setNextReservationDate(location: String) {
            itemView.search_result_current_reservation_date.text = location
        }

        fun setFavouriteSelected(isSet: Boolean) {
            itemView.search_result_fav_checkbox.visibility = if (isSet) View.VISIBLE else View.GONE

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchView {
        val from = LayoutInflater.from(context)
        val view = from.inflate(R.layout.avia_search_result, parent, false)

        return SearchView(view)
    }

    override fun getItemCount(): Int {
        return searchItems.size
    }

    override fun onBindViewHolder(holder: SearchView, position: Int) {
        val closestReservation =
            reservations.filter { it.classId.toString() == searchItems[position] }
                .minBy { it.endDate }
        val currentTime = System.currentTimeMillis()
        val entry = with(closestReservation) {
            when {
                this == null -> null
                currentTime in beginDate..endDate -> this
                else -> null
            }
        }
        holder.setClassNumber("Sala: ${searchItems[position]}")
        if (entry != null) {

            val fullName = if (entry.studentFullName.isEmpty()) "" else ", ${entry.studentFullName}"

            holder.setReservationTitle("${entry.title}$fullName")
            holder.setNextReservationDate("${entry.beginDate.dateFormattedHHmm()} - ${entry.endDate.dateFormattedHHmm()}")
            holder.setFavouriteSelected(false)
        } else {
            val calendar = Calendar.getInstance()
            holder.setReservationTitle("")

            if (calendar.get(Calendar.HOUR_OF_DAY) >= 20) {
                holder.setNextReservationDate("Instytut jest zamknięty, zapraszamy jutro.")
                holder.setFavouriteSelected(false)
            }
                else{
                holder.setNextReservationDate("Wolne teraz")
                holder.setFavouriteSelected(true)
            }
        }

        //holder.itemView.search_result_fav_checkbox.isSelected = entry.isFavourite
        //holder.setFavouriteSelected(entry.isFavourite)

        //holder.itemView.search_result_fav_checkbox.setOnCheckedChangeListener(null)

        with(holder.itemView) {
            setOnClickListener {
                val intent = Intent(context, ReservationSchedule::class.java).apply {
                    putExtra("classId", searchItems[position].toLong())
                }
                context.startActivity(intent)
            }
        }
    }

    fun updateSearchResults(results: List<String>) {
        searchItems = results
        notifyDataSetChanged()
    }
}
