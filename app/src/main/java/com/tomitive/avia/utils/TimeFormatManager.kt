package com.tomitive.avia.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeFormatManager {
    private val timeZone = TimeZone.getTimeZone("UTC")
    private val simpleDateFormat = SimpleDateFormat("E dd/MM/yyyy HH:mm:ss").also { it.timeZone = timeZone }


    val currentTime: String
    get() = "${simpleDateFormat.format(Calendar.getInstance().time)} (UTC)"
}