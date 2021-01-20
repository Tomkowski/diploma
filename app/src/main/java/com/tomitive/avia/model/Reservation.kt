package com.tomitive.avia.model

import java.util.*

data class Reservation(
    val classId: Long,
    val studentId: Long,
    val title: String,
    val beginDate: Calendar,
    val endDate: Calendar
)
var reservations: MutableList<Reservation> = mutableListOf()