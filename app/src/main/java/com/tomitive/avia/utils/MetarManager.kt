package com.tomitive.avia.utils

import android.util.Log
import android.widget.Toast
import com.tomitive.avia.interfaces.ForecastManager
import com.tomitive.avia.model.airports
import io.github.mivek.facade.MetarFacade
import io.github.mivek.facade.TAFFacade
import io.github.mivek.model.Metar
import io.github.mivek.model.TAF
import io.github.mivek.parser.MetarParser
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import org.jsoup.select.Elements
import java.lang.Exception
import kotlin.concurrent.thread
import kotlin.properties.Delegates

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
                        Jsoup.connect("http://awiacja.imgw.pl/rss/metarmil.php?airport=$airportName")
                            .get()

                    val rss =
                        Jsoup.parse(doc.html(), "", Parser.xmlParser()).select("description")

                    val textToDecode =
                        rss[METAR_POSITION_IN_RSS].text().trim().removePrefix("METAR")
                            .replace("COR", "").trim()
                    MetarFacade.getInstance().decode(textToDecode)
                } catch (exception: Exception) {
                    exception.printStackTrace()
                    null
                }

            }
        }.join()
        Log.d(TAG, "GOT $airportName")
        //Log.d(TAG, metar.toString())
        return metar
    }
}
