package com.tomitive.avia.utils

import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tomitive.avia.model.reservations

class ClassMarker(latLng: LatLng, classId: String,val floor: Int) {

    private val currentTime = System.currentTimeMillis()
    private val activeReservation = with(classId.toLong()){
        reservations.find { it.classId == this && it.beginDate < currentTime && currentTime < it.endDate }
    }
    private val markerColor = maxOf(0f, 120f - (activeReservation?.endDate?: currentTime - currentTime) / 60000f)
    val marker =  MarkerOptions()
        .position(latLng)
        .title(classId)
        .visible(false)
        .icon(BitmapDescriptorFactory.defaultMarker(markerColor)) ?: MarkerOptions()
}