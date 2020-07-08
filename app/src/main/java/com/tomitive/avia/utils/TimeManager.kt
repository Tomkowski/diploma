package com.tomitive.avia.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeManager {

    val time :String
        get() = SimpleDateFormat("MM.dd.yyyy HH:mm", Locale.ROOT).format(Calendar.getInstance().time)

}
