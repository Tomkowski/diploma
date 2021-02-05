package com.tomitive.avia.ui.reservation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import com.robinhood.ticker.TickerUtils
import com.tomitive.avia.MainActivity
import com.tomitive.avia.R
import com.tomitive.avia.api.RestApiService
import com.tomitive.avia.model.Credentials
import com.tomitive.avia.model.ReservationRequest
import com.tomitive.avia.utils.dateFormattedDDMMYYYY
import kotlinx.android.synthetic.main.activity_reservation.*
import java.util.*

class ReservationActivity : AppCompatActivity() {
    private var beginDate: Long = 0L
    private var classId: Long = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)

        with(intent.extras ?: return) {
            beginDate = getLong("beginDate")
            classId = getLong("classId")

            rangeSlider.apply {
                valueFrom = 0F
                valueTo = getLong("difference").toFloat()
                values = listOf(valueFrom, valueTo)
                stepSize = 900000F
            }
        }

        activity_reservation_details.text =
            "${beginDate.dateFormattedDDMMYYYY()}\nSala: $classId\n${if (classId > 100) "II" else "I"} piętro"

        with(activity_reservation_begin_date) {
            setCharacterLists(TickerUtils.provideNumberList())
            text = getSliderTime(rangeSlider.values[0])
        }
        with(activity_reservation_end_date) {
            setCharacterLists(TickerUtils.provideNumberList())
            text = getSliderTime(rangeSlider.values[1])
        }

        with(rangeSlider) {
            setLabelFormatter {
                val calendar =
                    Calendar.getInstance().apply { timeInMillis = beginDate + it.toLong() }
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)

                String.format("%02d:%02d", hour, minute)
            }

            addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: RangeSlider) {
                    activity_reservation_begin_date.text = getSliderTime(values[0])
                    activity_reservation_end_date.text = getSliderTime(values[1])
                }

                override fun onStopTrackingTouch(slider: RangeSlider) {
                    activity_reservation_begin_date.text = getSliderTime(values[0])
                    activity_reservation_end_date.text = getSliderTime(values[1])
                }
            })

//            addOnChangeListener { _, _, _ ->
//                activity_reservation_begin_date.text = getSliderTime(values[0])
//                activity_reservation_end_date.text = getSliderTime(values[1])
//            }

            reservation_button.setOnClickListener {
                with(rangeSlider.values) {
                    if (this[0] == this[1]) {
                        Toast.makeText(
                            this@ReservationActivity,
                            context.getString(R.string.minimum15minut),
                            Toast.LENGTH_LONG
                        ).show()
                        return@setOnClickListener
                    }
                }
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
                    val sliderValues = rangeSlider.values.map { it.toLong() }

                    val title = activity_reservation_title.text.toString()
                    val reservationRequest = ReservationRequest(
                        Credentials(username, password),
                        classId,
                        if (title.isEmpty()) "Rezerwacja użytkownika $username" else title,
                        beginDate = beginDate + sliderValues[0],
                        endDate = beginDate + sliderValues[1]
                    )
                    service.createReservation(reservationRequest) {
                        val intent =
                            Intent(this@ReservationActivity, MainActivity::class.java)
                                .apply {
                                    flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    putExtra("status", it)
                                }
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun getSliderTime(value: Float): String {
        val calendar = Calendar.getInstance().apply { timeInMillis = beginDate + value.toLong() }
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return String.format("%02d:%02d", hour, minute)
    }


}