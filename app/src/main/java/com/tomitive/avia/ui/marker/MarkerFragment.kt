package com.tomitive.avia.ui.marker

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.tomitive.avia.R
import com.tomitive.avia.databinding.MarkerFragmentBinding
import com.tomitive.avia.model.Reservation
import com.tomitive.avia.model.reservations
import com.tomitive.avia.ui.reservation.ReservationSchedule
import kotlinx.android.synthetic.main.marker_fragment.*
import java.util.*


class MarkerFragment : Fragment() {
    lateinit var code: String
    private lateinit var meteoWebView: WebView
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
                    title = "Wolne teraz",
                    studentFullName = "Możliwa rezerwacja",
                    beginDate = System.currentTimeMillis(),
                    endDate = System.currentTimeMillis()
                ),
                Reservation(
                    classId = code.toLong(),
                    title = "Koniec zajęć",
                    studentFullName = "",
                    beginDate = System.currentTimeMillis(),
                    endDate = System.currentTimeMillis()
                )
            )
        } else if (closestReservations.size == 1) {
            if(closestReservations[0].beginDate > System.currentTimeMillis()){
                closestReservations = listOf(
                    Reservation(
                        classId = code.toLong(),
                        title = "Wolne teraz",
                        studentFullName = "Możliwa rezerwacja",
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
                    title = "Koniec zajęć",
                    beginDate = closestReservations[0].endDate,
                    endDate = Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 20); set(Calendar.MINUTE, 0) }.timeInMillis
                )
            )
        }

        rootView.markerFragmentCurrentClass.reservation = closestReservations[0]
        rootView.markerFragmentNextClass.reservation = closestReservations[1]


        return rootView.root
    }

    override fun onStart() {
        super.onStart()
        meteo_button.setOnClickListener { initButton(code) }
    }

    private fun initButton(code: String) {
        val intent = Intent(context, ReservationSchedule::class.java).apply {
            putExtra("classId", code.toLong())
        }
        requireContext().startActivity(intent)
    }
}