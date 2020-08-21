package com.tomitive.avia.utils

import android.util.Log
import com.google.gson.*
import com.tomitive.avia.interfaces.ForecastManager
import com.tomitive.avia.model.Notam
import com.tomitive.avia.model.RawNotam
import com.tomitive.avia.model.notamCodes
import kotlinx.coroutines.runBlocking
import java.lang.reflect.Type
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
        val airportDecodedNotams = mutableListOf<Notam>()

        val deserializer = object : JsonDeserializer<Notam>{
            override fun deserialize(
                json: JsonElement?,
                typeOfT: Type?,
                context: JsonDeserializationContext?
            ): Notam {
                with(json!!.asJsonObject){
                    val startValidity = Date(1000L * get("startvalidity").asInt)
                    val endValidity = Date(1000L * (get("endvalidity").asInt))
                    val rawNotam = get("iteme").asString

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


                    return Notam(
                        airportName,
                        startValidity,
                        endValidity,
                        decodedNotam,
                        rawNotam
                    )
                }
            }
        }

        val customGson = GsonBuilder().registerTypeAdapter(Notam::class.java, deserializer).create()
        val gson = Gson()

        val rawApiObject: RawNotam = gson.fromJson(notams, RawNotam::class.java)
        rawApiObject.rows.forEach {
            val notam = customGson.fromJson(gson.toJson(it), Notam::class.java)
            airportDecodedNotams.add(notam)
        }


        return airportDecodedNotams
    }
}