package com.tomitive.avia.ui.reservation

import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tomitive.avia.R
import com.tomitive.avia.model.Reservation
import com.tomitive.avia.model.reservations
import com.tomitive.avia.utils.MarginItemDecoration
import com.tomitive.avia.utils.extensions.dateFormattedDDMMYYYY
import kotlinx.android.synthetic.main.activity_reservation_schedule.*
import java.util.*


/**
 * Aktywność prezentująca listę zajęć z możliwością rezerwacji oraz wyboru dnia
 */
class ReservationSchedule : AppCompatActivity() {

    /**
     * Obecnie wybrany dzień przez użytkownika. Dodanie 4 godzin wynika z zamknięcia rezerwacji na dany dzień o 20:00
     */
    private val dateSelected = Calendar.getInstance().apply { add(Calendar.HOUR, 4) }

    /**
     * numer sali wybrany przez użytkownika
     */
    private var classId: Long = 0L

    /**
     * lista reprezentująca plan zajęć
     */
    private lateinit var scheduleList: RecyclerView

    /**
     * metoda wywoływana przy stworzeniu aktywności
     *
     * @param savedInstanceState mapa klucz-wartość zapisanych danych w pamięci urządzenia
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_schedule)
        reservation_schedule_date.text = dateSelected.timeInMillis.dateFormattedDDMMYYYY()
        scheduleList = findViewById<RecyclerView>(R.id.reservation_schedule_recycler)
        with(intent.extras ?: return) {
            classId = getLong("classId")
        }
        initRecyclerView(scheduleList, createPlaceholders(reservationForSelectedDay(dateSelected)))
    }

    /**
     * metoda wywoływana po wybraniu dnia wcześniejszego
     *
     * @param v widok wciśnięty w celu wywołania metody
     */
    fun onLeftSelected(v: View) {
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        if(dateSelected.get(Calendar.DAY_OF_YEAR) > currentDay){
            dateSelected.add(Calendar.DAY_OF_YEAR, -1)
        }
        
        reservation_schedule_date.text = dateSelected.timeInMillis.dateFormattedDDMMYYYY()
        with((scheduleList.adapter as ReservationViewAdapter)) {
            data = createPlaceholders(reservationForSelectedDay(dateSelected))
            notifyDataSetChanged()
        }
    }

    /**
     * metoda wywoływana po wybraniu dnia przyszłego
     *
     * @param v widok wciśnięty w celu wywołania metody
     */
    fun onRightSelected(v: View) {
        dateSelected.add(Calendar.DAY_OF_YEAR, 1)
        reservation_schedule_date.text = dateSelected.timeInMillis.dateFormattedDDMMYYYY()
        with((scheduleList.adapter as ReservationViewAdapter)) {
            data = createPlaceholders(reservationForSelectedDay(dateSelected))
            notifyDataSetChanged()
        }
    }

    /**
     * metoda wywoływana po wybraniu konkretnego dnia z kalendarza
     *
     * @param v widok wciśnięty w celu wywołania metody
     */
    fun onDateClicked(v: View) {
        val picker = DatePicker(this).apply {
            with(dateSelected) {
                apply {
                    updateDate(get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DAY_OF_MONTH))
                    add(Calendar.HOUR, 4)
                }
                    minDate = System.currentTimeMillis() + 1000 * 60 * 60 * 4
            }
        }
        AlertDialog.Builder(this)
            .setView(picker)
            .setNegativeButton("Anuluj") { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton("OK") { dialog, _ ->
                dateSelected.apply {
                    set(Calendar.YEAR, picker.year)
                    set(Calendar.MONTH, picker.month)
                    set(Calendar.DAY_OF_MONTH, picker.dayOfMonth)
                    reservation_schedule_date.text =
                        dateSelected.timeInMillis.dateFormattedDDMMYYYY()

                    with((scheduleList.adapter as ReservationViewAdapter)) {
                        data = createPlaceholders(reservationForSelectedDay(dateSelected))
                        notifyDataSetChanged()
                    }
                }
            }
            .show()
    }

    /**
     * metoda zwracająca listę aktywności na wybrany dzień
     *
     * @param calendar dzień dla którego mają zostać zwrócone wszystkie jego aktywności
     */
    private fun reservationForSelectedDay(calendar: Calendar): List<Reservation> {
        val lowerBound = calendar.apply {
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val upperBound = lowerBound + 1000 * 60 * 60 * 24
        return reservations.filter { it.classId == classId && (lowerBound <= it.beginDate && it.endDate < upperBound) }
    }

    /**
     * Metoda zwracająca listę rezerwacji wypełniając dostępnymi terminami puste miejsca pomiędzy kolejnymi rezerwacjami
     *
     * @param reservationList lista rezerwacji, który ma być rozszerzona o dostępne termriny
     */
    private fun createPlaceholders(reservationList: List<Reservation>): List<Reservation> {
        val sortedList = reservationList.sortedBy { it.endDate }
        var start = dateSelected.apply {
            set(Calendar.HOUR_OF_DAY, 8)
        }.timeInMillis
        var end = start + 1000 * 60 * 60 * 12

        val dayEnd = end
        val resultList = mutableListOf<Reservation>()

        sortedList.forEach {
            end = it.beginDate
            if (start != end) {
                resultList.add(Reservation(classId = classId, beginDate = start, endDate = end))
            }
            resultList.add(it)
            start = it.endDate
        }

        if (start != dayEnd) resultList.add(
            Reservation(
                classId = classId,
                beginDate = start,
                endDate = dayEnd
            )
        )

        return resultList
    }

    /**
     * inicjuje plan zajęć. Przypisuje adapter do listy zajęć.
     *
     * @param searchList - plan zajęć
     * @param reservationList - lista rezerwacji i wolnych terminów na dany dzień
     */
    private fun initRecyclerView(searchList: RecyclerView, reservationList: List<Reservation>) {
        searchList.adapter = ReservationViewAdapter(this, reservationList)
        searchList.addItemDecoration(
            MarginItemDecoration(resources.getDimension(R.dimen.recyclerview_item_padding).toInt())
        )
        searchList.layoutManager = LinearLayoutManager(this)
        searchList.setHasFixedSize(true)
    }
}