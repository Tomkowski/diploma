package com.tomitive.avia.utils.extensions

import com.google.android.gms.maps.model.LatLng

/**
 * operacaja odejmowania od siebie współrzędnych geograficznych
 */
operator fun LatLng.minus(latLng: LatLng): LatLng{
    return LatLng(this.latitude - latLng.latitude, this.longitude - latLng.longitude)
}

/**
 * operacaja dodawania od siebie współrzędnych geograficznych
 */
operator fun LatLng.plus(latLng: LatLng): LatLng{
    return LatLng(this.latitude + latLng.latitude, this.longitude + latLng.longitude)
}

/**
 * operacaja mnożenia i dzielenia współrzędnych geograficznych przez skalar
 */
operator fun LatLng.div(value: Double): LatLng = LatLng(this.latitude / value, this.longitude / value)