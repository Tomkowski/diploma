package com.tomitive.avia.ui.favourites

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tomitive.avia.AirbaseDataFullInfo
import com.tomitive.avia.R
import com.tomitive.avia.databinding.AviaFavouriteItemBinding
import com.tomitive.avia.model.Airport
import com.tomitive.avia.utils.MetarManager
import com.tomitive.avia.utils.airportLocation
import com.tomitive.avia.utils.airportName
import io.github.mivek.model.Metar
import kotlinx.android.synthetic.main.avia_favourite_item.view.*
import kotlinx.android.synthetic.main.avia_favourite_item_status.view.*
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

class FavouritesViewAdapter(private val context: Context, private val data: List<Airport>) :
    RecyclerView.Adapter<FavouritesViewAdapter.FavouritesView>() {
    private val TAG = "MetarViewAdapter"

    class FavouritesView(private val binding: AviaFavouriteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val parentLayout = binding.root.findViewById<ConstraintLayout>(R.id.avia_favourite_item)

        fun bind(item: Airport) {
            binding.airport = item
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesView {

        val view = AviaFavouriteItemBinding.inflate(
            LayoutInflater.from(context), parent, false)

        return FavouritesView(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: FavouritesView, position: Int) {

        holder.bind(data[position])

        holder.parentLayout.setOnClickListener {

            Log.d(TAG, "Clicked on : $airportName")
            val intent = Intent(context, AirbaseDataFullInfo::class.java)
            intent.putExtra("airbaseFullName", data[position].airportFullName)
            intent.putExtra("airbaseName", data[position].airportName)
            context.startActivity(intent)
        }
        holder.itemView.animation =
            AnimationUtils.loadAnimation(context, R.anim.favourite_item_transition)
    }

}