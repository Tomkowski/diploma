package com.tomitive.avia.ui.reservation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.tomitive.avia.MainActivity
import com.tomitive.avia.R
import com.tomitive.avia.api.RestApiService
import com.tomitive.avia.model.Credentials
import com.tomitive.avia.model.Reservation
import com.tomitive.avia.model.ReservationRequest
import com.tomitive.avia.model.reservations
import kotlinx.android.synthetic.main.activity_reservation.*
import java.util.*

class ReservationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)

        with(intent.extras ?: return) {
            rangeSlider.apply {
                valueFrom = getFloat("beginDate") / 100000F
                valueTo = getFloat("endDate") / 100000F
                values = listOf(valueFrom, valueTo)
                stepSize = 9F
            }
        }

        with(rangeSlider) {
            setLabelFormatter {
                val calendar = Calendar.getInstance().apply { timeInMillis = it.toLong() * 100000 }
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)

                String.format("%02d:%02d", hour, minute);
            }
            reservation_button.setOnClickListener {
                val service = RestApiService()
                with(
                    getSharedPreferences(
                        getString(R.string.preferencesName),
                        Context.MODE_PRIVATE
                    )
                ) {
                    val username =
                        getString(getString(R.string.sharedUsername), "default") ?: "empty"
                    val password =
                        getString(getString(R.string.sharedPassword), "default") ?: "empty"
                    val sliderValues = rangeSlider.values.map { it.toLong() * 100000L }

                    val reservationRequest = ReservationRequest(
                        Credentials(username, password),
                        123L,
                        "$username's reservation",
                        beginDate = sliderValues[0],
                        endDate = sliderValues[1]
                    )
                    service.createReservation(reservationRequest) {
                        if (it == "200") {
                            with(reservationRequest) {
                                reservations.add(
                                    Reservation(
                                        classId,
                                        username.toLong(),
                                        title,
                                        Calendar.getInstance().apply { timeInMillis = beginDate },
                                        Calendar.getInstance().apply { timeInMillis = endDate }
                                    )
                                )
                            }
                            val intent = Intent(this@ReservationActivity, MainActivity::class.java)
                                .apply {
                                    flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                }
                            Log.d("reserv", "${reservations.size}")
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@ReservationActivity, "$it", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }
}