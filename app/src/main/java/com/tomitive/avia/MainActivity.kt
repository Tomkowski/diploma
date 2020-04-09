package com.tomitive.avia

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tomitive.avia.databinding.ActivityMainBinding
import com.tomitive.avia.model.TimeFormatManager
import io.github.mivek.facade.MetarFacade
import io.github.mivek.facade.TAFFacade
import io.github.mivek.model.TAF
import io.github.mivek.parser.TAFParser
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.net.URL


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = Navigation.findNavController(findViewById(R.id.nav_host_fragment))



        navView.setupWithNavController(navController)

        navView.setOnNavigationItemReselectedListener { }

        val format = "EEEE dd/MM/yyyy HH:mm:ss (UTC)"
        val timeZone = "GMT+000"

        binding.timeBar.timezone =
            TimeFormatManager(format, timeZone)




        /*

        //TODO export to model and logic
        Thread {
            try {
                val doc = Jsoup.connect("http://awiacja.imgw.pl/index.php?product=taf_mil").get()
                val elementsHtml: Elements? = doc.getElementsByClass("forecast")
                elementsHtml?.let {
                    Log.d("ELEMENT", elementsHtml.text())
                }

                val notam = URL("https://api.autorouter.aero/v1.0/notam?itemas=[\"EPDE\"]&offset=0&limit=10").readText()
                Log.d("ELEMENT", notam)

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }.start()


         */

        // val taf = TAFFacade.getInstance().decode("TAF EPDE 080800Z 0809/0818 09008KT CAVOK=")


        val m = "KMWL 111155Z 13012KT 8SM -TSRA SCT100CB OVC250 08/06 A2998 RMK RAzB32 OCNL LTGIC VC         SW-OHD TSB34 MOV NE P0012 T00780059 SLP150 10105 20052 60012"
        val metar = MetarFacade.getInstance().decode(m)

        Log.d("ELEMENT", metar.remark)




    }
}