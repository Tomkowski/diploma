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

private const val metarOk = 910


class FavouritesViewAdapter(
    private val context: Context,
    var data: MutableList<Reservation>
) :
    RecyclerView.Adapter<FavouritesViewAdapter.FavouritesView>() {
    private val TAG = "MetarViewAdapter"
    private lateinit var parent: RecyclerView
    private val cancelReservationDialog = AlertDialog.Builder(context)

    abstract class FavouritesView(binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        abstract val parentLayout: ConstraintLayout

        abstract fun bind(item: Reservation)
    }

    inner class MetarOkHolder(private val binding: UserReservationItemBinding) :
        FavouritesView(binding) {
        override val parentLayout: ConstraintLayout =
            binding.root.findViewById(R.id.user_reservation_card)

        override fun bind(item: Reservation) {
            with(parentLayout) {
                cancel_button.setOnClickListener { onClickMetarOk(adapterPosition) }
                animation = AnimationUtils.loadAnimation(context, R.anim.favourite_item_transition)

                if (adapterPosition < 3) {
                    animation.startOffset = (adapterPosition * 150L)
                }

            }

            with(binding) {
                reservation = item
                executePendingBindings()
            }
        }

        private fun onClickMetarOk(position: Int) {
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

    override fun getItemViewType(position: Int): Int {
        //if (data[position].reloading) return metarLoading
        //if (data[position].metar == null) return metarError


        return metarOk
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesView {
        val binding = UserReservationItemBinding.inflate(
            LayoutInflater.from(context), parent, false
        )
        return MetarOkHolder(binding)

    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: FavouritesView, position: Int) {
        holder.bind(data[position])
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        parent = recyclerView
    }

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
                data = reservations.filter { reservation ->  reservation.studentId == credentialUsername.toLong() } as MutableList<Reservation>

                notifyDataSetChanged()
            }
        }
    }
}