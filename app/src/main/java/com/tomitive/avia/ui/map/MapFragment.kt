package com.tomitive.avia.ui.map

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.DrawableRes
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.tomitive.avia.R
import com.tomitive.avia.interfaces.TransitionChangeListener
import com.tomitive.avia.ui.marker.MarkerFragment
import com.tomitive.avia.utils.*
import com.tomitive.avia.utils.extensions.div
import com.tomitive.avia.utils.extensions.minus
import com.tomitive.avia.utils.extensions.plus
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_map.view.*

/**
 * fragment wyświetlający mapę instytutu
 */
class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private val loggerTag = "gmap"
    private lateinit var mMap: GoogleMap

    /**
     * ograniczenie pola widzenia mapy. Lewy dolny róg
     */
    private val mapSouthWestBound = LatLng(-20.0, -24.0)
    /**
     * ograniczenie pola widzenia mapy. Prawy górny róg
     */
    private val mapNorthWestBound = LatLng(20.0, 24.0)

    /**
     * grafika przedstawiająca parter instytutu
     */
    private val groundFloorMap: BitmapDescriptor by lazy {
        getOverlay(R.drawable.map_ground_floor)
    }
    /**
     * grafika przedstawiająca pierwsze piętro instytutu
     */
    private val firstFloorMap: BitmapDescriptor by lazy {
        getOverlay(R.drawable.map_first_floor)
    }

    /**
     * minimalne przybliżenie kamery na mapie
     */
    private val minZoom = 4.0f

    /**
     * maksymalne przybliżenie kamery na mapie
     */
    private val maxZoom = 5.2f

    /**
     * lista sal instytutu i ich współrzędnych
     */
    private val classMarkers = listOf(
        ClassMarker(LatLng(-13.168386811560687, -6.694337725639343), "4", 0),
        ClassMarker(LatLng(-14.6015254863029, -10.801186971366405), "5", 0),
        ClassMarker(LatLng(-11.96325007924583, -14.505403116345407), "6", 0),
        ClassMarker(LatLng(-8.476876945462902, -13.109611049294472), "7", 0),
        ClassMarker(LatLng(-4.289742953927543, -13.458559066057207), "8", 0),
        ClassMarker(LatLng(0.32309115065473437, -13.163295798003675), "9", 0),

        ClassMarker(LatLng(-8.504819330368314, 13.57303272932768), "137", 1),
        ClassMarker(LatLng(-12.908260527768626, 14.67356152832508), "138", 1),
        ClassMarker(LatLng(-14.810593346442857, 11.049870662391186), "139", 1),
        ClassMarker(LatLng(-13.352645245465972, 7.023549042642117), "140", 1),
        ClassMarker(LatLng(-12.515498219020738, 3.6414383724331856), "141", 1),
        ClassMarker(LatLng(-12.397731158757427, -2.9563562572002415), "103", 1),
        ClassMarker(LatLng(-13.574712856750635, -6.606887988746165), "104", 1),
        ClassMarker(LatLng(-14.797794096529948, -10.445315502583982), "105", 1),
        ClassMarker(LatLng(-8.385521899121104, -13.263741172850132), "107", 1),
        ClassMarker(LatLng(-4.251181575607923, -13.371109329164028), "108", 1),
        ClassMarker(LatLng(-0.06771235081910691, -13.371109329164028), "109", 1),
        ClassMarker(LatLng(3.7947796101039066, -13.236898966133596), "110", 1)
    )

    /**
     * lista znaczników na mapie
     */
    private val markers = mutableListOf<Marker>()

    /**
     * obecnie wyświetlane piętro instytutu
     */
    private lateinit var currentMapOverlay: GroundOverlay

    /**
     * metoda wywoływana przy stworzeniu fragmentu
     * @param savedInstanceState mapa klucz-wartość zapisanych danych w pamięci urządzenia
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_map, container, false)
        root.fragment_map_motion_layout.setTransitionListener(
            object : TransitionChangeListener {
                override fun onTransitionChange(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int,
                    progress: Float
                ) {
                    //drawable end
                    val classTitleDrawableEnd = fragment_map_class_title.compoundDrawables[2]

                    if (endId == R.id.fragment_map_scene_end) {
                        classTitleDrawableEnd?.alpha = (255f - 255f * progress).toInt()
                    }
                }

                override fun onTransitionCompleted(motionLayout: MotionLayout?, transitionId: Int) {
                    //drawable end
                    val classTitleDrawableEnd = fragment_map_class_title.compoundDrawables[2]

                    if (transitionId == R.id.fragment_map_scene_start)
                        classTitleDrawableEnd?.alpha = 255
                }
            }
        )
        return root
    }

    /**
     * metoda wywoływana przy stworzeniu aktywności, w której występuje fragment
     * @param savedInstanceState mapa klucz-wartość zapisanych danych w pamięci urządzenia
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        map_view.onCreate(savedInstanceState)
        map_view.onResume()
        map_view.getMapAsync(this)
        openMarkerFragment("1")
        setUpButtons()
    }

    /**
     * metoda wywoływana w momencie utworzenia obiektu mapy
     */
    override fun onMapReady(map: GoogleMap?) {
        if (map == null) return

        mMap = map
        with(mMap) {

            setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))
            currentMapOverlay = addGroundOverlay(
                GroundOverlayOptions()
                    .image(groundFloorMap)
                    .positionFromBounds(LatLngBounds(mapSouthWestBound, mapNorthWestBound))
            )
            Log.d(loggerTag, "map ready")
            classMarkers.forEach {
                markers.add(addMarker(it.marker).apply { isVisible = (it.floor == 0) })
            }

            setOnMarkerClickListener(this@MapFragment)
            setLatLngBoundsForCameraTarget(
                LatLngBounds
                    (
                    mapSouthWestBound,
                    mapNorthWestBound
                )
            )

            setMinZoomPreference(minZoom)
            setMaxZoomPreference(maxZoom)
            moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    mapSouthWestBound + (mapNorthWestBound - mapSouthWestBound) / 2.0,
                    minZoom
                )
            )
        }

    }

    /**
     * metoda wywoływana po wciśnięciu na znacznik
     *
     * @param marker wciśnięty znacznik
     */
    override fun onMarkerClick(marker: Marker?): Boolean {
        if (marker == null) return true

        val currentTime = System.currentTimeMillis()
        val activeReservation = with(marker.title.toLong()) {
            com.tomitive.avia.model.reservations.find { it.classId == this && it.beginDate < currentTime && currentTime < it.endDate }
        }
        marker.snippet = activeReservation?.title ?: getString(R.string.free_now)
        marker.showInfoWindow()
        with(marker) {
            Handler().post {
                moveToCurrentLocation(LatLng(position.latitude, position.longitude))
            }
        }

        fragment_map_class_title.text = marker.title
        openMarkerFragment(marker.title)
        toolbarAction()
        return true
    }

    /**
     * otwiera fragment wyświetlający szczegóły sali
     *
     * @param classId numer sali
     */
    private fun openMarkerFragment(classId: String) {
        val args = Bundle().apply { putString("title", classId) }
        childFragmentManager
            .beginTransaction()
            .replace(R.id.marker_fragment, MarkerFragment().apply {
                arguments = args
            }, "MarkerFragment")
            .commit()

    }

    /**
     * przesuwa kamerę do wybranej pozycji
     *
     * @param currentLocation wybrana pozycja
     */
    private fun moveToCurrentLocation(currentLocation: LatLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 13f))
    }

    /**
     * metoda wysuwa dolny pasek wyświetlający [MarkerFragment]
     *
     */
    private fun toolbarAction() {
        val button = view?.findViewById<Button>(R.id.dummy_motion_listener) ?: return
        button.performClick()
    }

    /**
     * metoda chowa dolny pasek wyświetlający [MarkerFragment]
     */
    fun hideToolbar() {
        val button = view?.findViewById<Button>(R.id.dummy_motion1_listener) ?: return
        button.performClick()

    }

    /**
     * metoda przypisuje logikę do przycisków
     */
    private fun setUpButtons() {
        with(ground_floor_button) {
            isClickable = false
            backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.reservation_card_color)

            setOnClickListener {
                currentMapOverlay.setImage(groundFloorMap)

                rootView.first_floor_button.isClickable = true
                rootView.first_floor_button.backgroundTintList =
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

                rootView.ground_floor_button.isClickable = true
                rootView.ground_floor_button.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.colorWhite)
            }
        }
    }

    /**
     * metoda zwraca bitmapę na podstawie ID
     *
     * @param resId ID bitmapy
     */
    private fun getOverlay(@DrawableRes resId: Int): BitmapDescriptor {
        Log.d(loggerTag, "get overlay $resId")
        return BitmapDescriptorFactory.fromResource(resId)
    }
}