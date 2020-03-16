package com.tomitive.avia.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.tomitive.avia.R
import com.tomitive.avia.utils.div
import com.tomitive.avia.utils.minus
import com.tomitive.avia.utils.plus
import kotlinx.android.synthetic.main.fragment_map.*


class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val polandSouthWest = LatLng(48.857389,  14.194399)
    private val polandNorthEast = LatLng(54.581568,  24.683870)

    private val defaultZoom = 5.7f

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        map_view.onCreate(savedInstanceState)
        map_view.onResume()
        map_view.getMapAsync(this)

    }

   override fun onMapReady(map: GoogleMap?){
        map?.let {

            mMap = it
            mMap.setLatLngBoundsForCameraTarget(LatLngBounds
                (polandSouthWest,
                polandNorthEast)
            )
            mMap.setMinZoomPreference(defaultZoom)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(polandSouthWest + (polandNorthEast - polandSouthWest) / 2.0, defaultZoom));  //move camera to location
        }
    }
}
