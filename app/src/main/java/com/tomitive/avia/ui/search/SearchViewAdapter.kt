package com.tomitive.avia.ui.search

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tomitive.avia.R
import com.tomitive.avia.model.classrooms
import com.tomitive.avia.model.reservations
import com.tomitive.avia.ui.reservation.ReservationSchedule
import com.tomitive.avia.utils.extensions.dateFormattedHHmm
import kotlinx.android.synthetic.main.uwr_search_result.view.*
import java.util.*

/**
 * Adapter listy sal we fragmencie SearchFragment
 *
 * @property context - aktywność, w której występuje adapter
 * @property searchItems - lista wszystkich sal instytutu
 */
class SearchViewAdapter(
    private val context: Context,
    private var searchItems: List<String> = classrooms
) : RecyclerView.Adapter<SearchViewAdapter.SearchView>() {

    /**
     * Klasa reprezentująca element listy sal
     *
     * @constructor
     *
     * @param itemView - układ reprezentujący opis sali
     */
    class SearchView(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /**
         * ustawia numer sali
         * @param id numer sali
         */
        fun setClassNumber(id: String) {
            itemView.search_result_classroom_number.text = id
        }

        /**
         * ustawia tytuł rezerwacji
         *
         * @param title tytuł rezerwacji
         */
        fun setReservationTitle(title: String) {
            itemView.search_result_current_reservation_title.text = title
        }

        /**
         * ustawia termin rezerwacji
         *
         * @param date termin rezerwacji
         */
        fun setReservationDate(date: String) {
            itemView.search_result_current_reservation_date.text = date
        }

        /**
         * ustawia znacznik dostępności sali
         *
         * @param available czy sala jest dostępna
         */
        fun setReservationAvailable(available: Boolean) {
            itemView.search_result_fav_checkbox.visibility = if (available) View.VISIBLE else View.GONE

        }

    }


    /**
     * Metoda wywołująca się w momencie tworzenia elementu listy
     *
     * @param parent układ, w którym wyświetlana jest lista
     * @param viewType typ elementu, który zostanie wyświetlony
     * @return
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchView {
        val from = LayoutInflater.from(context)
        val view = from.inflate(R.layout.uwr_search_result, parent, false)

        return SearchView(view)
    }

    override fun getItemCount() = searchItems.size

    /**
     * metoda wywoływana podczas przypisania wartości dla elementu listy
     *
     * @param holder - widok reprezentujący element listy
     * @param position - pozycja elementu na liscie
     */
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
            holder.setReservationDate("${entry.beginDate.dateFormattedHHmm()} - ${entry.endDate.dateFormattedHHmm()}")
            holder.setReservationAvailable(false)
        } else {
            val calendar = Calendar.getInstance()
            holder.setReservationTitle("")

            if (calendar.get(Calendar.HOUR_OF_DAY) >= 20) {
                holder.setReservationDate(context.getString(R.string.closed_come_back_tomorrow))
                holder.setReservationAvailable(false)
            } else {
                holder.setReservationDate(context.getString(R.string.free_now))
                holder.setReservationAvailable(true)
            }
        }

        with(holder.itemView) {
            setOnClickListener {
                val intent = Intent(context, ReservationSchedule::class.java).apply {
                    putExtra("classId", searchItems[position].toLong())
                }
                context.startActivity(intent)
            }
        }
    }

    /**
     * nadpisuje listę sal do wyświetlenia
     *
     * @param results lista sal do wyświetlenia
     */
    fun updateSearchResults(results: List<String>) {
        searchItems = results
        notifyDataSetChanged()
    }
}
