package com.tomitive.avia.model

import com.google.gson.annotations.SerializedName

/**
 * klasa przechowująca informacje o żądaniu zarezerwowania terminu
 *
 * @property requester dane użytkownika rezerwującego
 * @property classId numer sali
 * @property title tytuł rezerwacji
 * @property beginDate data rozpoczęcia rezerwacji
 * @property endDate data zakończenia rezerwacji
 */
data class ReservationRequest(
    @SerializedName("requester")val requester: Credentials,
    @SerializedName("classId")val classId: Long,
    @SerializedName("title")val title: String,
    @SerializedName("beginDate")val beginDate: Long,
    @SerializedName("endDate")val endDate: Long
)
