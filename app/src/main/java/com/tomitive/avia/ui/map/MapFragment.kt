package com.tomitive.avia.ui.map

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.tomitive.avia.R
import com.tomitive.avia.model.airports
import com.tomitive.avia.ui.marker.MarkerFragment
import com.tomitive.avia.utils.*
import kotlinx.android.synthetic.main.fragment_map.*


class MapFragment() : Fragment(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    private val TAG = "gmap"
    private lateinit var mMap: GoogleMap
    private val polandSouthWest = LatLng(-10.0, -10.0)
    private val polandNorthEast = LatLng(10.0, 10.0)
    private val groundFloorMap: BitmapDescriptor by lazy {
        getOverlay(R.drawable.map)
    }
    private val firstFloorMap: BitmapDescriptor by lazy {
        getOverlay(R.drawable.first_floor)
    }

    private val minZoom = 4.0f
    private val maxZoom = 5.2f

    private val classMarkers = listOf(
        ClassMarker(LatLng(0.0, 0.0), "103", 1),
        ClassMarker(LatLng(1.0, 1.0), "104", 1),
        ClassMarker(LatLng(2.0, 2.0), "105", 1),
        ClassMarker(LatLng(2.0, 0.0), "106", 1),
        ClassMarker(LatLng(0.0, 0.0), "3", 0),
        ClassMarker(LatLng(3.0, 0.0), "4", 0),
        ClassMarker(LatLng(3.0, 1.0), "5", 0),
        ClassMarker(LatLng(-1.0, 2.0), "7", 0)
    )

    private val markers = mutableListOf<Marker>()

    private lateinit var currentMapOverlay: GroundOverlay

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
        setUpButtons()
    }

    override fun onMapReady(map: GoogleMap?) {
        if (map == null) return

        mMap = map
        with(mMap) {
            currentMapOverlay = addGroundOverlay(
                GroundOverlayOptions()
                    .image(groundFloorMap)
                    .positionFromBounds(LatLngBounds(polandSouthWest, polandNorthEast))
            )
            setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))
            Log.d(TAG, "map ready")
            classMarkers.forEach {
                markers.add(addMarker(it.marker).apply { isVisible = (it.floor == 0) })
            }

            setOnMarkerClickListener(this@MapFragment)
            setLatLngBoundsForCameraTarget(
                LatLngBounds
                    (
                    polandSouthWest,
                    polandNorthEast
                )
            )

            setMinZoomPreference(minZoom)
            setMaxZoomPreference(maxZoom)
            moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    polandSouthWest + (polandNorthEast - polandSouthWest) / 2.0,
                    5.0f
                )
            )
        }

    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        if (marker == null) return true
        Log.d(TAG, "clicked on ${marker.title}")
        with(marker) {
            Handler().post {
                moveToCurrentLocation(LatLng(position.latitude, position.longitude))
            }
        }

        airport_title.text = marker.title
        with(childFragmentManager.findFragmentByTag("MarkerFragment") as MarkerFragment) {
            code = marker.title
            loadUrl(code)
        }
        toolbarAction()
        return true
    }

    private fun openMarkerFragment(airportTitle: String) {
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

    private fun toolbarAction() {
        val button = view?.findViewById<Button>(R.id.dummy_motion_listener) ?: return
        button.performClick()
    }

    fun hideToolbar() {
        val button = view?.findViewById<Button>(R.id.dummy_motion1_listener) ?: return
        button.performClick()

    }

    private fun setUpButtons() {
        with(ground_floor_button) {
            isClickable = false
            backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.reservation_card_color)

            setOnClickListener {
                currentMapOverlay.setImage(groundFloorMap)

                first_floor_button.isClickable = true
                first_floor_button.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.colorWhite)

                markers.forEachIndexed { index, elem ->
                    elem.isVisible = (classMarkers[index].floor == 0)
                }
                backgroundTintList =
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.reservation_card_color
                    )
                isClickable = false

                Toast.makeText(context, "Ground floor selected", Toast.LENGTH_SHORT).show()
            }
        }
        with(first_floor_button) {
            setOnClickListener {
                currentMapOverlay.setImage(firstFloorMap)

                isClickable = false
                backgroundTintList =
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.reservation_card_color
                    )

                markers.forEachIndexed { index, elem ->
                    elem.isVisible = (classMarkers[index].floor == 1)
                }

                ground_floor_button.isClickable = true
                ground_floor_button.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.colorWhite)

                Toast.makeText(context, "First floor selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getOverlay(@DrawableRes resId: Int): BitmapDescriptor {
        Log.d(TAG, "get overlay $resId")
        return BitmapDescriptorFactory.fromResource(resId)
    }
}