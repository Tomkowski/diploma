package com.tomitive.avia.utils

import android.util.Log
import com.tomitive.avia.interfaces.ForecastManager
import io.github.mivek.facade.TAFFacade
import io.github.mivek.model.TAF
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import kotlin.properties.Delegates

class TafManager : ForecastManager<TAF?> {

    // TODO decoded EPDA is unknown as an airport
    private var taf: List<TAF> by Delegates.notNull()

    init {
        val doc = Jsoup.connect("http://awiacja.imgw.pl/index.php?product=taf_mil").get()
        val elementsHtml: Elements? = doc.getElementsByClass("forecast")
        elementsHtml?.let {

            Log.d("beforeMapping", elementsHtml.text())
            taf =  elementsHtml.text()
                .split("=")
                .dropLast(1)
                .map {TAFFacade.getInstance().decode("${it.trim()}=")
                }
        }
    }

    override fun getForecast(airportName: String): TAF?{
        return null
    }

}

/*
 /*

        //TODO export to model and logic



         */

        // val taf = TAFFacade.getInstance().decode("TAF EPDE 080800Z 0809/0818 09008KT CAVOK=")


        val m = "KMWL 111155Z 13012KT 8SM -TSRA SCT100CB OVC250 08/06 A2998 RMK RAzB32 OCNL LTGIC VC         SW-OHD TSB34 MOV NE P0012 T00780059 SLP150 10105 20052 60012"
        val metar = MetarFacade.getInstance().decode(m)

        Log.d("ELEMENT", metar.remark)

        */