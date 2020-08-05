package com.tomitive.avia.utils

import android.util.Log
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser
import com.tomitive.avia.interfaces.ForecastManager
import com.tomitive.avia.model.Notam
import com.tomitive.avia.model.notamCodes
import kotlinx.coroutines.runBlocking
import java.net.URL
import java.util.*
import kotlin.concurrent.thread

object NotamManager : ForecastManager<List<Notam>> {
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


        Log.d("NotamManager", jsonObject)
        val notams = jsonObject ?: ""
        if (notams.isEmpty()) return emptyList()

        val mapper = Klaxon()
        val json = mapper.parse<Map<String, Any>>(notams)

        val jsonArray = json?.get("rows") as JsonArray<JsonObject>?


        Log.d("NotamManager", "${jsonArray?.get(0)?.get("iteme")}")
        val airportDecodedNotams = mutableListOf<Notam>()

        jsonArray?.forEach { notam ->

            val startValidity = Date(1000L * (notam["startvalidity"] as Int).toLong())
            val endValidity = Date(1000L * (notam["endvalidity"] as Int).toLong())
            val iteme = notam["iteme"] as String

            val decodedNotam = iteme.split(" ", "\t", ":").map { word ->
                notamCodes[word.trim()]?.toUpperCase(
                    Locale.ROOT
                )
                    ?: word
            }.joinToString(separator = " ")
            Log.d("NotamManager", decodedNotam)

            airportDecodedNotams.add(
                Notam(
                    airportName,
                    startValidity,
                    endValidity,
                    decodedNotam,
                    iteme
                )
            )
        }
        return airportDecodedNotams
    }
}