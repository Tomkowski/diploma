package com.tomitive.avia.utils

import com.google.android.gms.maps.model.LatLng

operator fun LatLng.minus(latLng: LatLng): LatLng{
    return LatLng(this.latitude - latLng.latitude, this.longitude - latLng.longitude)
}

operator fun LatLng.plus(latLng: LatLng): LatLng{
    return LatLng(this.latitude + latLng.latitude, this.longitude + latLng.longitude)
}

operator fun LatLng.div(value: Double): LatLng = LatLng(this.latitude / value, this.longitude / value)