package com.tomitive.avia.ui.map

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.tomitive.avia.MainActivity
import com.tomitive.avia.R
import com.tomitive.avia.model.airports
import com.tomitive.avia.ui.marker.MarkerFragment
import com.tomitive.avia.utils.airportLocationCoordinates
import com.tomitive.avia.utils.div
import com.tomitive.avia.utils.minus
import com.tomitive.avia.utils.plus
import kotlinx.android.synthetic.main.avia_favourite_item.*
import kotlinx.android.synthetic.main.fragment_map.*


class MapFragment() : Fragment(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

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
        openMarkerFragment("EPDE")
    }

    override fun onMapReady(map: GoogleMap?) {
        if (map == null) return

        mMap = map
        with(mMap) {
            setOnMarkerClickListener(this@MapFragment)
            setLatLngBoundsForCameraTarget(
                LatLngBounds
                    (
                    polandSouthWest,
                    polandNorthEast
                )
            )


            val zoom = (if (inPortraitMode) portraitZoom else landscapeZoom)
            setMinZoomPreference(zoom)
            moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    polandSouthWest + (polandNorthEast - polandSouthWest) / 2.0,
                    zoom
                )
            );  //move camera to location
        }

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
        }

        airport_title.text = marker.title
        with(childFragmentManager.findFragmentByTag("MarkerFragment") as MarkerFragment){
            code = marker.title
            loadUrl(code)
        }
        toolbarAction()
        return true
    }

    private fun openMarkerFragment(airportTitle: String){
        val args = Bundle().apply { putString("title", airportTitle) }
        childFragmentManager
            .beginTransaction()
            .replace(R.id.meteo, MarkerFragment().apply {
                arguments = args
            }, "MarkerFragment")
            .commit()

    }

    private fun moveToCurrentLocation(currentLocation: LatLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 13f))
    }

    fun refreshMap() {
        val zoom = (if (inPortraitMode) portraitZoom else landscapeZoom)
        mMap.setMinZoomPreference(zoom)
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                polandSouthWest + (polandNorthEast - polandSouthWest) / 2.0,
                zoom
            )
        );  //move camera to location
    }
    private fun toolbarAction(){
        val button = view?.findViewById<Button>(R.id.dummy_motion_listener) ?: return
        button.performClick()
    }
    fun hideToolbar(){
        val button = view?.findViewById<Button>(R.id.dummy_motion1_listener) ?: return
        button.performClick()

    }
}