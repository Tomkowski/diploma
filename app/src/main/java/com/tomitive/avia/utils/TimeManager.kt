package com.tomitive.avia.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeManager {

    val currentTime :String
        get() = SimpleDateFormat("dd.MM.yyyy - HH:mm", Locale.ROOT).format(Calendar.getInstance().time)

}

fun Date.parseToString(): String{
    return SimpleDateFormat("dd.MM.yyyy - HH:mm", Locale.ROOT).format(this)
}