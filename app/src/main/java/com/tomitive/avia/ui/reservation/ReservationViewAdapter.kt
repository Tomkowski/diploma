package com.tomitive.avia.ui.reservation

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.tomitive.avia.MainActivity
import com.tomitive.avia.R
import com.tomitive.avia.databinding.ReservationPlaceholderActiveBinding
import com.tomitive.avia.databinding.ReservationPlaceholderTakenBinding
import com.tomitive.avia.model.Reservation
import kotlinx.android.synthetic.main.reservation_placeholder_active.view.*

/**
 * Adapter odpowiedzialny za wyświetlanie elementów listy planu zajęć sali.
 * @property context - aktywność w której adapter zostaje wywołany
 * @property data - lista rezerwacji wyświetlana w planie zajęć.
 */
class ReservationViewAdapter(private val context: Context, var data: List<Reservation>) :
    RecyclerView.Adapter<ReservationViewAdapter.PlaceholderView>() {
    private val placeholderTakenID = 100
    private val placeholderActiveID = 200

    /**
     * Abstrakcyjna klasa reprezentująca element listy rezerwacji
     * @param binding obiekt klasy reprezentującej układ elementu listy
     */
    abstract class PlaceholderView(binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val parentLayout: ConstraintLayout =
            binding.root.findViewById(R.id.reservation_placeholder_layout)

        abstract fun bind(item: Reservation)
    }

    /**
     * Klasa reprezentująca aktywną rezerwację na liście zajęć
     */
    inner class ReservationTaken(private val binding: ReservationPlaceholderTakenBinding) :
        ReservationViewAdapter.PlaceholderView(binding) {
        override fun bind(item: Reservation) {
            parentLayout.animation = AnimationUtils.loadAnimation(context, R.anim.fragment_fade_enter)
            with(binding) {
                reservation = item
                executePendingBindings()
            }
        }
    }

    /**
     * Klasa reprezentująca dostępną rezerwację na liście zajęć.
     *
     * @property binding
     * @constructor Create empty Reservation placeholder
     */
    inner class ReservationPlaceholder(private val binding: ReservationPlaceholderActiveBinding) :
        ReservationViewAdapter.PlaceholderView(binding) {

        override fun bind(item: Reservation) {
            parentLayout.animation = AnimationUtils.loadAnimation(context, R.anim.fragment_fade_enter)
            parentLayout.reservation_placeholder_active_button.setOnClickListener {
                val intent = Intent(context, ReservationActivity::class.java).apply {
                    putExtra("beginDate", item.beginDate)
                    putExtra("difference", item.endDate - item.beginDate)
                    putExtra("classId", item.classId)
                }
                context.startActivity(intent)
            }
            with(binding) {
                reservation = item
                executePendingBindings()
            }
        }
    }

    /**
     * Zwraca typ elementu, który ma zostać wyświetlony na liście zajęć.
     *
     * @param position pozycja elementu na liście
     * @return
     */
    override fun getItemViewType(position: Int): Int {
        return if (data[position].id == -1L) placeholderActiveID else placeholderTakenID
    }

    /**
     * Metoda wywołująca się w momencie tworzenia elementu listy
     *
     * @param parent układ, w którym wyświetlana jest lista
     * @param viewType typ elementu, który zostanie wyświetlony
     * @return
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceholderView {
        return when (viewType) {
            placeholderActiveID -> {
                val binding = ReservationPlaceholderActiveBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
                ReservationPlaceholder(binding)
            }
            else -> {
                val binding = ReservationPlaceholderTakenBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
                ReservationTaken(binding)
            }
        }
    }

    /**
     * metoda wywoływana podczas przypisania wartości dla elementu listy
     *
     * @param holder - widok reprezentujący element listy
     * @param position - pozycja elementu na liscie
     */
    override fun onBindViewHolder(holder: PlaceholderView, position: Int) {
        holder.bind(data[position])
    }

    /**
     * Zwaraca ilość elementów do wyświetlenia
     */
    override fun getItemCount(): Int = data.size
}