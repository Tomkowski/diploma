package com.tomitive.avia.utils

import android.util.Log
import com.tomitive.avia.interfaces.ForecastManager
import com.tomitive.avia.model.Notam
import com.tomitive.avia.model.notamCodes
import kotlinx.coroutines.runBlocking
import java.net.URL
import java.util.*
import kotlin.concurrent.thread

object NotamManager : ForecastManager<List<Notam>> {
    private const val TAG = "NotamManager"
    override fun getForecast(airportName: String): List<Notam> {
        var jsonObject: String? = null
        Log.d(TAG, "$airportName")

        thread {

            runBlocking {
                jsonObject = try {
                    val doc =
                        URL(
                            "https://api.autorouter.aero/v1.0/notam?itemas=[\"$airportName\"]"
                        ).readText(charset = Charsets.UTF_16)

                    doc
                } catch (exception: Exception) {
                    exception.printStackTrace()
                    null
                }

            }
        }.join()


        val notams = jsonObject ?: ""
        if (notams.isEmpty()) return emptyList()

        Log.d(TAG, jsonObject)
        val iteme = Regex("(?<=\"iteme\":\")((.+?)(?=\"))")

        val startValidityRegex = Regex("(?<=\"startvalidity\":)((.+?)(?=,))")
        val endValidityRegex = Regex("(?<=\"endvalidity\":)((.+?)(?=,))")

        val airportDecodedNotams = mutableListOf<Notam>()

        val rawNotams = iteme.findAll(notams).map { it.value }.toList()
        val sValidity = startValidityRegex.findAll(notams).map { it.value.toLong() }.toList()
        val eValidity = endValidityRegex.findAll(notams).map { it.value.toLong() }.toList()

        val dataLength = rawNotams.size


        for (i in 0 until dataLength) {


            val startValidity = Date(1000L * (sValidity[i]))
            val endValidity = Date(1000L * (eValidity[i]))
            val rawNotam = rawNotams[i]

            Log.d(TAG, rawNotam)
            val decodedNotam = rawNotam.trim().let {
                var fixedNotam = ""
                it.forEach { char ->
                    fixedNotam += if (char.isLetterOrDigit()) char else " $char"
                }
                fixedNotam
            }.split(" ", "\t").map { word ->

                val trimWord = word.trim()

                val decodedWord = notamCodes[trimWord] ?: return@map word

                return@map (if (word.endsWith("\n")) "$decodedWord\n" else decodedWord).toUpperCase(
                    Locale.ROOT
                )

            }.joinToString(separator = " ")
                .let {

                    var joinDots = ""
                    val length = it.length
                    it.forEachIndexed { index, c ->
                        if (index < length - 1)
                            with(c) {
                                if (!c.isWhitespace()) joinDots += this
                                else if (it[index + 1].isLetterOrDigit()) joinDots += this
                            }
                    }
                    joinDots
                }

            Log.d(TAG, decodedNotam)

            airportDecodedNotams.add(
                Notam(
                    airportName,
                    startValidity,
                    endValidity,
                    decodedNotam,
                    rawNotam
                )
            )
        }
        return airportDecodedNotams
    }
}