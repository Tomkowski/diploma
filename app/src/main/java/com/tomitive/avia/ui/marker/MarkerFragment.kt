package com.tomitive.avia.ui.marker

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.tomitive.avia.R
import com.tomitive.avia.databinding.MarkerFragmentBinding
import com.tomitive.avia.model.Reservation
import com.tomitive.avia.model.reservations
import com.tomitive.avia.ui.reservation.ReservationSchedule
import kotlinx.android.synthetic.main.marker_fragment.*
import java.util.*

/**
 * fragment wyświetlany jako szczegóły sali po wybraniu znacznika na mapie
 */
class MarkerFragment : Fragment() {
    /**
     * numer sali
     */
    private lateinit var code: String

    /**
     * metoda wywoływana przy stworzeniu fragmentu
     * @param savedInstanceState mapa klucz-wartość zapisanych danych w pamięci urządzenia
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        code = arguments?.getString("title") ?: ""

        val rootView =
            DataBindingUtil.inflate<MarkerFragmentBinding>(
                inflater,
                R.layout.marker_fragment,
                container,
                false
            )

        var closestReservations =
            reservations.filter { it.classId == code.toLong() && it.endDate > System.currentTimeMillis() }
                .sortedBy { it.endDate }.take(2)
        if (closestReservations.isEmpty()) {
            closestReservations = listOf(
                Reservation(
                    classId = code.toLong(),
                    title = getString(R.string.free_now),
                    studentFullName = getString(R.string.available_reservation),
                    beginDate = Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 8); set(Calendar.MINUTE, 0) }.timeInMillis,
                    endDate = Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 20); set(Calendar.MINUTE, 0) }.timeInMillis
                ),
                Reservation(
                    classId = code.toLong(),
                    title = getString(R.string.uni_closed),
                    studentFullName = "",
                    beginDate = Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 20); set(Calendar.MINUTE, 0) }.timeInMillis,
                    endDate = Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 8); set(Calendar.MINUTE, 0) }.timeInMillis
                )
            )
        } else if (closestReservations.size == 1) {
            if(closestReservations[0].beginDate > System.currentTimeMillis()){
                closestReservations = listOf(
                    Reservation(
                        classId = code.toLong(),
                        title = getString(R.string.free_now),
                        studentFullName = getString(R.string.available_reservation),
                        beginDate = System.currentTimeMillis(),
                        endDate = closestReservations[0].beginDate
                    ),
                    closestReservations[0]
                )
            }
            else
            closestReservations = listOf(
                closestReservations[0],
                Reservation(
                    classId = code.toLong(),
                    studentFullName = "",
                    title = getString(R.string.free_now),
                    beginDate = closestReservations[0].endDate,
                    endDate = Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 20); set(Calendar.MINUTE, 0) }.timeInMillis
                )
            )
        }

        rootView.markerFragmentCurrentClass.reservation = closestReservations[0]
        rootView.markerFragmentNextClass.reservation = closestReservations[1]


        return rootView.root
    }


    /**
     * metoda wywołująca się przy starcie fragmentu
     */
    override fun onStart() {
        super.onStart()
        marker_reservation_button.setOnClickListener { openReservationActivity() }
    }

    /**
     * metoda rozpoczynająca menu rezerwacji dla wybranej sali
     */
    private fun openReservationActivity() {
        val intent = Intent(context, ReservationSchedule::class.java).apply {
            putExtra("classId", code.toLong())
        }
        requireContext().startActivity(intent)
    }
}