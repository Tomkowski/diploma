package com.tomitive.avia.utils

import android.util.Log
import com.tomitive.avia.interfaces.ForecastManager
import com.tomitive.avia.model.airports
import io.github.mivek.facade.MetarFacade
import io.github.mivek.model.Metar
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import kotlin.concurrent.thread

object MetarManager : ForecastManager<Metar?> {
    private val TAG = "metar"
    private val METAR_POSITION_IN_RSS = 1

    override fun getForecast(airportName: String): Metar? {
        var metar: Metar? = null
        thread {

            runBlocking {
                Log.d(TAG, "Started searching for $airportName")
                metar = try {
                    val doc =
                        Jsoup.connect(
                            "http://awiacja.imgw.pl/rss/metarmil.php?airport=$airportName"
                        ).get()

                    val rss =
                        Jsoup.parse(doc.html(), "", Parser.xmlParser()).select("description")

                    val textToDecode =
                        rss[METAR_POSITION_IN_RSS].text().trim().removePrefix("METAR")
                            .replace("COR", "").trim()

                    airports.find { it.airportName == airportName }?.rawMetar = textToDecode

                    MetarFacade.getInstance().decode(textToDecode)
                } catch (exception: Exception) {
                    exception.printStackTrace()
                    null
                }

            }
        }.join()
        Log.d(TAG, "GOT $airportName")
        return metar
    }
}
