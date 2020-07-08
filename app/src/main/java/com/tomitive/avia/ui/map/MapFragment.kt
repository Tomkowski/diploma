package com.tomitive.avia.ui.map

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.system.Os.remove
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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
        try {
            map_view.onCreate(savedInstanceState)
            map_view.onResume()
            map_view.getMapAsync(this)

        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        try {
            map?.let {
                mMap = it
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
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}