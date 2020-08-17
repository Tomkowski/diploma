package com.tomitive.avia.utils

import io.github.mivek.enums.CloudQuantity

object CloudManager {

    fun calculateCloudCoverage(quantity: CloudQuantity, height: Int): String {
        return when (quantity) {
            CloudQuantity.FEW -> "1-2/8 Few clouds"
            CloudQuantity.SCT -> "3-4/8 Scattered"
            CloudQuantity.BKN -> "5-7/8 Broken ceiling"
            CloudQuantity.OVC -> "8/8 Overcast"
            else -> return "No significant clouds"
        } +  " at ${height}ft"
    }
}