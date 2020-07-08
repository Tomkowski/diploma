package com.tomitive.avia.ui.favourites

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.tomitive.avia.AirbaseDataFullInfo
import com.tomitive.avia.R
import com.tomitive.avia.databinding.AviaFavouriteItemBinding
import com.tomitive.avia.databinding.FavouriteItemErrorBinding
import com.tomitive.avia.databinding.FavouriteItemLoadingBinding
import com.tomitive.avia.model.Airport
import com.tomitive.avia.utils.MetarManager
import com.tomitive.avia.utils.TimeManager
import com.tomitive.avia.utils.airportName
import kotlinx.android.synthetic.main.avia_favourite_item.view.*
import kotlinx.android.synthetic.main.avia_favourite_item_status.view.*
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

private const val metarError = 900
private const val metarLoading = 909
private const val metarOk = 910


class FavouritesViewAdapter(private val context: Context, private val data: List<Airport>) :
    RecyclerView.Adapter<FavouritesViewAdapter.FavouritesView>() {
    private val TAG = "MetarViewAdapter"
    private lateinit var parent: RecyclerView

    abstract class FavouritesView(binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        abstract val parentLayout: ConstraintLayout

        abstract fun bind(item: Airport)
    }

    inner class MetarOkHolder(private val binding: AviaFavouriteItemBinding) :
        FavouritesView(binding) {
        override val parentLayout: ConstraintLayout =
            binding.root.findViewById(R.id.avia_favourite_item)

        override fun bind(item: Airport) {

            with(parentLayout) {
                setOnClickListener { onClickMetarOk(adapterPosition) }
                animation = AnimationUtils.loadAnimation(context, R.anim.favourite_item_transition)

                if(adapterPosition < 3){
                    animation.startOffset = (adapterPosition * 150L)
                }

            }

            with(binding) {
                airport = item
                flightRule.flightStatus = "LIFR"
                flightRule.root.setBackgroundResource(R.drawable.shape_avia_favourite_status_background)
                executePendingBindings()
            }
        }

        private fun onClickMetarOk(position: Int) {
            Log.d(TAG, "Clicked on : $airportName")
            val intent = Intent(context, AirbaseDataFullInfo::class.java)
            intent.putExtra("airbaseFullName", data[position].airportFullName)
            intent.putExtra("airbaseName", data[position].airportName)
            context.startActivity(intent)
        }
    }

    inner class MetarErrorHolder(private val binding: FavouriteItemErrorBinding) :
        FavouritesView(binding) {
        override val parentLayout: ConstraintLayout =
            binding.root.findViewById(R.id.avia_favourite_item_error)

        override fun bind(item: Airport) {
            with(parentLayout) {
                setOnClickListener {
                    with(data[adapterPosition]) {
                        reloading = true
                        notifyItemChanged(adapterPosition)
                    }
                }
                alpha = 0.8f
            }
            with(binding) {
                airport = item
                executePendingBindings()
            }
        }
    }

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
                        metar?.let { timestamp = TimeManager.time }
                        reloading = false
                        parent.post {
                            notifyItemChanged(adapterPosition)
                        }
                    }
                }
            }
        }

    }


    override fun getItemViewType(position: Int): Int {
        if (data[position].reloading) return metarLoading
        if (data[position].metar == null) return metarError


        return metarOk
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesView {

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
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: FavouritesView, position: Int) {
        holder.bind(data[position])
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        parent = recyclerView
    }
}