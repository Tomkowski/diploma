package com.tomitive.avia.utils

import io.github.mivek.model.Metar

enum class FlightRule(val description: String){
    LIFR("Low Instrument Flight Rules"),
    IFR("Instrument Flight Rules"),
    MVFR("Marginal VFR"),
    VFR("Visual Flight Rules"),
    NODATA("No data available"){
        override fun toString(): String {
            return "No data"
        }
    };

    override fun toString(): String{
        return name
    }
}

object FlightRuleManager {

    fun calculateFlightRule(metar: Metar): FlightRule{
        val ceiling = metar.clouds.map { it.height }.min() ?: 10000

        val visibility = when(metar.visibility.mainVisibility){
            null -> return FlightRule.NODATA
            ">10km" -> 10000
            else -> {metar.visibility.mainVisibility.dropLast(1).toInt()}
        }

        if(ceiling >= 3000 && visibility >= 5000 ) return FlightRule.VFR
        if(ceiling >= 1000 || visibility >= 3000) return FlightRule.MVFR
        if(ceiling >= 500 || visibility >= 1000) return FlightRule.IFR
        return FlightRule.LIFR
    }

}