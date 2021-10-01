package com.tomitive.avia.utils

import android.util.Log
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tomitive.avia.model.reservations

/**
 * Klasa reprezentująca znaczniki sal wyświetlane na mapie instytutu
 *
 * @property floor piętro na którym występuje znacznik (parter lub 1. piętro)
 *
 * @param latLng współrzędne geograficzne znacznika
 * @param classId numer sali
 */
class ClassMarker(latLng: LatLng, classId: String, val floor: Int) {

    private val currentTime = System.currentTimeMillis()

    /**
     * aktualna rezerwacja w danej sali (o ile istnieje)
     */
    private val activeReservation = with(classId.toLong()) {
        reservations.find { it.classId == this && it.beginDate < currentTime && currentTime < it.endDate }
    }

    /**
     *Zwraca kolor markera na mapie bazując na czasie pozostałym do zakończenia aktualnej rezerwacji [activeReservation.endDate].
     *120 dla koloru zielonego, 0 dla czerwonego.
     *Pomiędzy natężenie koloru przechodzi liniowo.
     */
    private val markerColor = maxOf(
        0f,
        120f - ((activeReservation?.endDate ?: currentTime) - currentTime) / 60000f
    )

    /**
     * opcje znacznika wyświetlanego na mapie
     */
    val marker = MarkerOptions()
        .position(latLng)
        .title(classId)
        .visible(false)
        .icon(BitmapDescriptorFactory.defaultMarker(markerColor)) ?: MarkerOptions()
}