package com.tomitive.avia.utils

import android.util.Log
import com.tomitive.avia.interfaces.ForecastManager
import io.github.mivek.facade.MetarFacade
import io.github.mivek.facade.TAFFacade
import io.github.mivek.model.Metar
import io.github.mivek.model.TAF
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import kotlin.concurrent.thread
import kotlin.properties.Delegates

class MetarManager : ForecastManager<Metar> {
    // TODO decoded EPDA is unknown as an airport
    private var metar: List<Metar> by Delegates.notNull()
    private var ready = false
    init {
        thread {
            runBlocking {
                val doc = Jsoup.connect("http://awiacja.imgw.pl/index.php?product=metar_mil").get()
                val elementsHtml: Elements? = doc.getElementsByClass("forecast")
                elementsHtml?.let {

                    Log.d("beforeMapping", elementsHtml.text())
                    metar = elementsHtml.text()
                        .split("=")
                        .dropLast(1)
                        .map {
                            Log.d("Mapping", it.trim().removePrefix("METAR"))
                            MetarFacade.getInstance().decode("${it.trim().removePrefix("METAR").trim()}=")
                        }
                }
            }
        }.join()
    }

    override val forecast: List<Metar>
        get() = metar

}
