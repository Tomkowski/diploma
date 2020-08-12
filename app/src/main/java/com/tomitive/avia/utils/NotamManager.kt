package com.tomitive.avia.utils

import android.util.Log
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
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
        thread {

            runBlocking {
                jsonObject = try {
                    val doc =
                        URL(
                            "https://api.autorouter.aero/v1.0/notam?itemas=[\"$airportName\"]"
                        ).readText()

                    doc
                } catch (exception: Exception) {
                    exception.printStackTrace()
                    null
                }

            }
        }.join()


        val notams = jsonObject ?: ""
        if (notams.isEmpty()) return emptyList()

        val mapper = Klaxon()
        val json = mapper.parse<Map<String, Any>>(notams)

        val jsonArray = json?.get("rows") as JsonArray<JsonObject>?


        val airportDecodedNotams = mutableListOf<Notam>()

        jsonArray?.forEach { notam ->

            val startValidity = Date(1000L * (notam["startvalidity"] as Int).toLong())
            val endValidity = Date(1000L * (notam["endvalidity"] as Int).toLong())
            val rawNotam = notam["iteme"] as String

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