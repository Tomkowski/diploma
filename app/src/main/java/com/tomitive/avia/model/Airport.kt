package com.tomitive.avia.model

import com.tomitive.avia.utils.airportLocation
import com.tomitive.avia.utils.airportName
import io.github.mivek.model.Metar
import io.github.mivek.model.TAF

data class Airport(
    val airportName: String,
    val airportFullName: String,
    val airportLocation: String?
) {
    var timestamp: String? = "Server not responding"
    var rawMetar: String? = null
    var metar: Metar? = null
    var taf: TAF? = null
    var notams: List<Notam> = emptyList()
    var isFavourite = false
    var reloading = true

}


//TEMPORARY HERE
var airports: List<Airport> = emptyList()