package com.tomitive.avia.ui.favourites

import android.content.Context
import android.content.Intent
import android.util.Log
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
import com.tomitive.avia.databinding.AviaFavouriteItemBinding
import com.tomitive.avia.databinding.FavouriteItemErrorBinding
import com.tomitive.avia.model.CancellationRequest
import com.tomitive.avia.model.Credentials
import com.tomitive.avia.model.Reservation
import com.tomitive.avia.model.reservations
import com.tomitive.avia.ui.airbasefullinfo.AirbaseDataFullInfo
import com.tomitive.avia.utils.airportName
import kotlinx.android.synthetic.main.avia_favourite_item.view.*

private const val metarError = 900
private const val metarLoading = 909
private const val metarOk = 910


class FavouritesViewAdapter(private val context: Context, private var data: MutableList<Reservation>) :
    RecyclerView.Adapter<FavouritesViewAdapter.FavouritesView>() {
    private val TAG = "MetarViewAdapter"
    private lateinit var parent: RecyclerView

    abstract class FavouritesView(binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        abstract val parentLayout: ConstraintLayout

        abstract fun bind(item: Reservation)
    }

    inner class MetarOkHolder(private val binding: AviaFavouriteItemBinding) :
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
            val service = RestApiService()
            with(context as MainActivity) {
                val classId = data[position].id
                service.cancelReservation(
                    CancellationRequest(Credentials(fetchUsername(), fetchToken()), classId)
                ) {
                    when (it) {
                        "414" -> {
                            Toast.makeText(this, "Twoja sesja wygasła", Toast.LENGTH_SHORT).show()
                            logout()
                        }
                        "400" -> {
                            Toast.makeText(
                                this,
                                "Coś poszło nie tak, spróbuj ponownie później. Kod błedu: $it",
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

    inner class MetarErrorHolder(private val binding: FavouriteItemErrorBinding) :
        FavouritesView(binding) {
        override val parentLayout: ConstraintLayout =
            binding.root.findViewById(R.id.avia_favourite_item_error)

        override fun bind(item: Reservation) {
            with(parentLayout) {
                setOnClickListener {
                    with(data[adapterPosition]) {
                        //reloading = true
                        notifyItemChanged(adapterPosition)
                    }
                }
                alpha = 0.8f
            }
            with(binding) {
                //airport = item
                executePendingBindings()
            }
        }
    }

    /*
    inner class MetarLoadingHolder(private val binding: FavouriteItemLoadingBinding) :
        FavouritesView(binding) {
        override val parentLayout: ConstraintLayout =
            binding.root.findViewById(R.id.avia_favourite_item_loading)

        override fun bind(item: Airport) {
            with(binding) {
                airport = item
                executePendingBindings()
            }
            with(data[adapterPosition]) {
                thread {
                    runBlocking {
                        metar = MetarManager.getForecast(airportName)
                        if (metar != null) {
                            timestamp = TimeManager.currentTime
                        }
                        reloading = false
                        parent.post {
                            notifyItemChanged(adapterPosition)
                        }
                    }
                }
            }
        }

    }
    */


    override fun getItemViewType(position: Int): Int {
        //if (data[position].reloading) return metarLoading
        //if (data[position].metar == null) return metarError


        return metarOk
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesView {
        val binding = AviaFavouriteItemBinding.inflate(
            LayoutInflater.from(context), parent, false
        )
        return MetarOkHolder(binding)

/*
        when (viewType) {
            metarOk -> {
                val binding = AviaFavouriteItemBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
                return MetarOkHolder(binding)

            }

            metarLoading -> {
                val binding = FavouriteItemLoadingBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
                return MetarLoadingHolder(binding)
            }
            else -> {
                val binding =
                    FavouriteItemErrorBinding.inflate(
                        LayoutInflater.from(context),
                        parent,
                        false
                    )
                return MetarErrorHolder(binding)
            }

        }
  */
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: FavouritesView, position: Int) {
        holder.bind(data[position])
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        parent = recyclerView
    }
}