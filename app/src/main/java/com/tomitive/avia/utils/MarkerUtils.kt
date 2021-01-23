package com.tomitive.avia.utils

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ClassMarker(latLng: LatLng, classId: String,val floor: Int) {
    val marker = MarkerOptions()
        .position(latLng)
        .title(classId)
        .visible(false) ?: MarkerOptions()
}