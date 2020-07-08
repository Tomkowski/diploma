package com.tomitive.avia.ui.map

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.tomitive.avia.R
import com.tomitive.avia.model.airports
import com.tomitive.avia.utils.airportLocationCoordinates
import com.tomitive.avia.utils.div
import com.tomitive.avia.utils.minus
import com.tomitive.avia.utils.plus
import kotlinx.android.synthetic.main.fragment_map.*


class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private val TAG = "gmap"
    private lateinit var mMap: GoogleMap
    private val polandSouthWest = LatLng(48.857389, 13.894399)
    private val polandNorthEast = LatLng(54.581568, 23.883870)

    private val portraitZoom = 5.7f
    private val landscapeZoom = 5.2f
    private val inPortraitMode: Boolean
        get() = (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)

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

    override fun onMapReady(map: GoogleMap?) {
        if (map == null) return

        mMap = map
        mMap.setOnMarkerClickListener(this)
        mMap.setLatLngBoundsForCameraTarget(
            LatLngBounds
                (
                polandSouthWest,
                polandNorthEast
            )
        )
        val zoom = (if (inPortraitMode) portraitZoom else landscapeZoom)
        mMap.setMinZoomPreference(zoom)
        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                polandSouthWest + (polandNorthEast - polandSouthWest) / 2.0,
                zoom
            )
        );  //move camera to location

        airports.filter { it.isFavourite }.forEach {
            val coordinates = airportLocationCoordinates[it.airportName]
            if (coordinates != null) {
                mMap.addMarker(
                    MarkerOptions().position(
                        coordinates
                    )
                        .title(it.airportName)
                )
            }
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        if (marker == null) return true

        Log.d(TAG, "clicked on ${marker.title}")
        with(marker) {
            Handler().post {
                moveToCurrentLocation(LatLng(position.latitude, position.longitude))
                showInfoWindow()
            }
            return true
        }
    }

    private fun moveToCurrentLocation(currentLocation: LatLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 13f))
    }

}