package com.tomitive.avia.ui.reservations

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.tomitive.avia.MainActivity
import com.tomitive.avia.R
import com.tomitive.avia.api.RestApiService
import com.tomitive.avia.databinding.UserReservationItemBinding
import com.tomitive.avia.model.*
import kotlinx.android.synthetic.main.user_reservation_item.view.*

/**
 * ID przypisane widokowi rezerwacji użytkownika
 */
private const val activeReservation = 910

/**
 * Adapter listy sal we fragmencie [ReservationsFragment]
 *
 * @property context - aktywność, w której występuje adapter
 * @property data - lista wszystkich rezerwacji użytkownika
 */
class ReservationsViewAdapter(
    private val context: Context,
    var data: MutableList<Reservation>
) :
    RecyclerView.Adapter<ReservationsViewAdapter.ReservationsView>() {
    private val TAG = "ReservationViewAdapter"
    private lateinit var parent: RecyclerView

    /**
     * dialog potwierdzający anulowanie rezerwacji
     */
    private val cancelReservationDialog = AlertDialog.Builder(context)

    /**
     * abstrakcyjna klasa reprezentująca układ wyświetlany na liście rezerwacji
     */
    abstract class ReservationsView(binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        /**
         * układ wyświetlanego elementu
         */
        abstract val parentLayout: ConstraintLayout

        /**
         * metoda przypisująca logikę elementowi listy
         *
         * @param item
         */
        abstract fun bind(item: Reservation)
    }

    /**
     * klasa reprezentująca aktywną rezerwację wyświetlaną na liście rezerwacji
     *
     * @property binding obiekt Databinding widoku rodzica
     */
    inner class ReservationHolder(private val binding: UserReservationItemBinding) :
        ReservationsView(binding) {
        override val parentLayout: ConstraintLayout =
            binding.root.findViewById(R.id.user_reservation_card)

        override fun bind(item: Reservation) {
            with(parentLayout) {
                cancel_button.setOnClickListener { onReservationCancelClick(adapterPosition) }
                animation = AnimationUtils.loadAnimation(context, R.anim.favourite_item_transition)

                if (adapterPosition < 8) {
                    animation.startOffset = (adapterPosition * 150L)
                }

            }

            with(binding) {
                reservation = item
                executePendingBindings()
            }
        }

        /**
         * metoda wykonywana po wciśnięciu przycisku anulowania rezerwacji
         *
         * @param position pozycja elementu na liście
         */
        private fun onReservationCancelClick(position: Int) {
            cancelReservationDialog.setMessage(context.getString(R.string.cancel_reservation_details) + "\n" + reservations[position].title)
                .setTitle(context.getString(R.string.cancel_reservation_dialog_title))
                .setPositiveButton(context.getString(R.string.yes)) { _, _ ->
                    cancelReservation(position)
                }
                .setNegativeButton(context.getString(R.string.no)) { dialog, _ ->
                    dialog.cancel()
                }
                .show()

        }
    }

    /**
     * zwraca typ widoku na pozycji [position]
     *
     * @param position pozycja elementu na liście rezerwacji
     */
    override fun getItemViewType(position: Int): Int {
        return activeReservation
    }

    /**
     * metoda wykonywana po utworzeniu widoku listy rezerwacji
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationsView {
        val binding = UserReservationItemBinding.inflate(
            LayoutInflater.from(context), parent, false
        )
        return ReservationHolder(binding)
    }

    /**
     * metoda zwracająca ilość elementów listy rezerwacji
     */
    override fun getItemCount(): Int = data.size

    /**
     * metoda wywoływana w momencie nadpisania widoku nowymi danymi
     */
    override fun onBindViewHolder(holder: ReservationsView, position: Int) {
        holder.bind(data[position])
    }

    /**
     * metoda wywoływana po przyłączeniu adaptera do [recyclerView]
     *
     * @param recyclerView lista rezerwacji
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        parent = recyclerView
    }

    /**
     * logika anulowania rezerwacji
     *
     * @param position pozycja elementu na liście rezerwacji
     */
    private fun cancelReservation(position: Int) {
        val service = RestApiService()
        with(context as MainActivity) {
            val classId = data[position].id
            service.cancelReservation(
                CancellationRequest(Credentials(fetchUsername(), fetchToken()), classId)
            ) {
                when (it) {
                    "414" -> {
                        Toast.makeText(this, getString(R.string.end_session), Toast.LENGTH_SHORT)
                            .show()
                        logout()
                    }
                    "400" -> {
                        Toast.makeText(
                            this,
                            getString(R.string.something_went_wrong_code) + it,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                downloadReservations()
                data = reservations
                notifyDataSetChanged()
            }
        }
    }
}