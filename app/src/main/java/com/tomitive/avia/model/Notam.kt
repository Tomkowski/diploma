package com.tomitive.avia.model

import java.util.*

data class Notam(
    val airportName: String,
    val startValidity: Date,
    val endValidity: Date,
    val description: String,
    val rawNotam: String
)