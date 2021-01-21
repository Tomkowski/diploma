package com.tomitive.avia.utils

import java.text.SimpleDateFormat
import java.util.*

fun Long.dateFormatted(): String{

    return SimpleDateFormat("HH:mm", Locale.ROOT).format(this)
}