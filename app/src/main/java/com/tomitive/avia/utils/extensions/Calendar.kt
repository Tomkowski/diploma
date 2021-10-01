package com.tomitive.avia.utils.extensions

import java.text.SimpleDateFormat
import java.util.*

/**
 * zwraca godzinę i minuty timestampu
 */
fun Long.dateFormattedHHmm(): String{

    return SimpleDateFormat("HH:mm", Locale.ROOT).format(this)
}

/**
 * Zwraca datę w formacie dd.mm.yyyy timestampu
 */
fun Long.dateFormattedDDMMYYYY(): String{

    return SimpleDateFormat("dd.MM.yyyy", Locale.ROOT).format(this)
}