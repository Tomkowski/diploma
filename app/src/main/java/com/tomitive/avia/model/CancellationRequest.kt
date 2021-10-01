package com.tomitive.avia.model

import com.google.gson.annotations.SerializedName

/**
 * Klasa przechowująca żądzenia anulowania rezerwacji
 * @property requester dane użytkownika wysyłającego żądanie
 * @property reservationId ID rezerwacji
 */
data class CancellationRequest(
    @SerializedName("requester") val requester: Credentials,
    @SerializedName("reservationId") val reservationId: Long
)