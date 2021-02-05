package com.tomitive.avia.utils

import java.text.SimpleDateFormat
import java.util.*

fun Long.dateFormattedHHmm(): String{

    return SimpleDateFormat("HH:mm", Locale.ROOT).format(this)
}

fun Long.dateFormattedDDMMYYYY(): String{

    return SimpleDateFormat("dd.MM.yyyy", Locale.ROOT).format(this)
}