package com.tomitive.avia.ui.favourites

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tomitive.avia.AirbaseDataFullInfo
import com.tomitive.avia.R
import com.tomitive.avia.utils.airportLocation
import com.tomitive.avia.utils.airtportName
import io.github.mivek.model.Metar
import io.github.mivek.parser.TAFParser
import kotlinx.android.synthetic.main.avia_favourite_item.view.*
import kotlinx.android.synthetic.main.avia_favourite_item_status.view.*

class FavouritesViewAdapter(private val context: Context, private val data: List<Metar>) : RecyclerView.Adapter<FavouritesViewAdapter.FavouritesView>() {
    private val TAG = "MetarViewAdapter"
    class FavouritesView(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val parentLayout = itemView.findViewById<ConstraintLayout>(R.id.avia_favourite_item)

        fun setAirportName(airportName: String?){
            itemView.airport_name.text = airportName
        }


        fun setAirportLocation(country: String?, city: String?){
            val location ="$city, $country"
            itemView.airport_location.text = location
        }

        fun setAirportLocation(location: String?){
            itemView.airport_location.text = location
        }

        fun setWind(speed: Int?, direction: String?){
            val wind = "$direction, $speed"
            itemView.wind.text = wind
        }

        fun setTemperature(temperature: Int?){
            itemView.temperature.text = temperature.toString()
        }

        fun setClouds(clouds: String?){
            itemView.clouds.text = clouds
        }

        fun setWeather(weather: String?){
            itemView.weather.text = weather
        }

        fun setVisibility(visibility: String?){
            itemView.weather.text = visibility
        }

        fun setFlightRules(rule: String?){
            itemView.flight_rule.status.text = "$rule"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesView {
        val from = LayoutInflater.from(context)
        val view = from.inflate(R.layout.avia_favourite_item, parent, false)

        return FavouritesView(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: FavouritesView, position: Int) {
        val metar: Metar = data[position]
        Log.d(TAG, metar.toString())
        Log.d(TAG, "creating fav view")
        val airportName = airtportName[metar.station]
        val airportLocation = airportLocation[metar.station]

        holder.setAirportName(airportName)
        holder.setAirportLocation(airportLocation)

        //check if COR
        //holder.setFlightRules(TAFParser.getInstance().parse("").isAmendment)
            /*
        holder.setWind(metar.wind.speed, metar.wind.direction)
        holder.setTemperature(metar.temperature)
        holder.setClouds(metar.clouds.joinToString(separator = ", "))
        holder.setWeather(metar.weatherConditions.joinToString(separator = ", "))
        holder.setVisibility(metar.visibility.mainVisibility)
        */
        holder.setFlightRules("VFR") //TODO logic to handle flight rule based on weather conditions

        holder.parentLayout.setOnClickListener {

            Log.d(TAG, "Clicked on : $airportName")
            val intent = Intent(context, AirbaseDataFullInfo::class.java)
            intent.putExtra("airbaseName", airportName)
            intent.putExtra("metar", metar.message)
            context.startActivity(intent)
        }
    }

}