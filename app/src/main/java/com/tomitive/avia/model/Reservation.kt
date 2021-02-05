package com.tomitive.avia.model

data class Reservation(
    val id: Long = -1L,
    val classId: Long,
    val studentId: Long = -1L,
    val studentFullName: String = "",
    val title: String = "",
    val beginDate: Long,
    val endDate: Long
)
var reservations: MutableList<Reservation> = mutableListOf()