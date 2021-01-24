package com.tomitive.avia.ui.reservation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.robinhood.ticker.TickerUtils
import com.tomitive.avia.MainActivity
import com.tomitive.avia.R
import com.tomitive.avia.api.RestApiService
import com.tomitive.avia.model.Credentials
import com.tomitive.avia.model.ReservationRequest
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

        with(activity_reservation_begin_date){
            setCharacterLists(TickerUtils.provideNumberList())
            text = getSliderTime(rangeSlider.values[0])
        }
        with(activity_reservation_end_date){
            setCharacterLists(TickerUtils.provideNumberList())
            text = getSliderTime(rangeSlider.values[1])
        }

        with(rangeSlider) {
            setLabelFormatter {
                val calendar = Calendar.getInstance().apply { timeInMillis = it.toLong() }
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)

                String.format("%02d:%02d", hour, minute)
            }

            addOnChangeListener { _, _, _ ->
                activity_reservation_begin_date.text = getSliderTime(values[0])
                activity_reservation_end_date.text = getSliderTime(values[1])
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
                    val sliderValues = rangeSlider.values.map { it.toLong() }

                    val reservationRequest = ReservationRequest(
                        Credentials(username, password),
                        classId,
                        activity_reservation_title.text.toString(),
                        beginDate = beginDate + sliderValues[0],
                        endDate = beginDate + sliderValues[1]
                    )
                    service.createReservation(reservationRequest) {
                        if (it == "200") {
                            val intent = Intent(this@ReservationActivity, MainActivity::class.java)
                                .apply {
                                    flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    putExtra("update made by user", true)
                                }
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

    private fun getSliderTime(value: Float): String {
        val calendar = Calendar.getInstance().apply { timeInMillis = beginDate + value.toLong() }
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return String.format("%02d:%02d", hour, minute)
    }
}