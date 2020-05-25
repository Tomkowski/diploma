package com.tomitive.avia.utils

import android.util.Log
import android.widget.Toast
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
    private var metar: MutableList<Metar> = mutableListOf()
    init {
        thread {
            runBlocking {
                try {
                    val doc = Jsoup.connect("http://awiacja.imgw.pl/index.php?product=metar_mil").get()
                    Log.d("metar", "Doc is null? ${doc == null}")
                    val elementsHtml: Elements? = doc.getElementsByClass("forecast")
                    elementsHtml?.let {

                        Log.d("beforeMapping", elementsHtml.text())
                        elementsHtml.text()
                            .split("=")
                            .dropLast(1)
                            .forEach {
                                try{
                                    metar.add(MetarFacade.getInstance()
                                        .decode("${it.trim().removePrefix("METAR").replace("COR","").trim()}="))
                                }
                                catch (exception : Exception){
                                    exception.printStackTrace()
                                }
                            }
                        /*

                         METAR COR EPPW 300830Z 15010KT 9999 NSC 14/06 Q1008 RMK 141 058 5/2=
                         is not parsable by decoder. Reason: COR as a name of an airport
                         */

                    }
                }
                catch (exception: Exception){
                    exception.printStackTrace()
                }
            }
        }.join()
        Log.d("metar", "finished downloading")
        Log.d("metar", "metar size is: ${metar.size}")
    }

    override val forecast: List<Metar>
        get() = metar

}
