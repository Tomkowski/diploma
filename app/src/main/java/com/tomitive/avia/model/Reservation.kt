package com.tomitive.avia.model

import com.google.gson.annotations.SerializedName

/**
 * klasa przechowująca informacje o rezerwacji
 *
 * @property id ID rezerwacji
 * @property classId numer sali
 * @property studentId login studenta - właściciela rezerwacji
 * @property studentFullName imię i nazwisko rezerwującego
 * @property title tytuł rezerwacji
 * @property beginDate data rozpoczęcia rezerwacji
 * @property endDate data zakończenia rezerwacji
 */
data class Reservation(
    val id: Long = -1L,
    val classId: Long,
    val studentId: Long = -1L,
    val studentFullName: String = "",
    val title: String = "",
    val beginDate: Long,
    val endDate: Long
)

/**
 * lista wszystkich rezerwacji pobranych z serwera
 */

var reservations: MutableList<Reservation> = mutableListOf()