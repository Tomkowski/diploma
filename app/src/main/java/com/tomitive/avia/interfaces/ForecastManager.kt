package com.tomitive.avia.interfaces

interface ForecastManager<T> {

   fun getForecast(airportName: String): T
}