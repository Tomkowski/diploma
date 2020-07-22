package com.tomitive.avia.ui.marker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tomitive.avia.ui.airbasefullinfo.AirbaseDataFullInfo
import com.tomitive.avia.R
import com.tomitive.avia.utils.NetworkManager
import com.tomitive.avia.utils.airportMeteoLinks
import com.tomitive.avia.utils.airportName
import kotlinx.android.synthetic.main.marker_fragment.*


class MarkerFragment : Fragment() {
    lateinit var code: String
    private lateinit var meteoWebView: WebView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("Marker", "view oncreate")
        code = arguments?.getString("title") ?: ""

        val rootView = inflater.inflate(R.layout.marker_fragment, container, false)

        initWebWiev(rootView)

        return rootView
    }

    override fun onStart() {
        super.onStart()
        meteo_button.setOnClickListener { initButton(code) }
    }

    private fun initWebWiev(rootView: View) {
        meteoWebView = rootView.findViewById<WebView>(R.id.meteo_web_view)
        with(meteoWebView.settings) {
            setAppCachePath("${requireContext().cacheDir.absolutePath}/aviacast")
            allowFileAccess = true
            setAppCacheEnabled(true)
            javaScriptEnabled = true
            cacheMode = WebSettings.LOAD_DEFAULT
            if (!NetworkManager.isNetworkAvailable(requireContext())) {
                cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            }
            loadUrl(code)
        }

         }

    fun loadUrl(code: String){
        var airportId = airportMeteoLinks[code]

        if (airportId == null) {
            Toast.makeText(requireContext(), "Unable to find that airport!", Toast.LENGTH_SHORT)
                .show()
            airportId = "1391"
        }
        meteoWebView.webViewClient = MeteoWebViewClient()
        meteoWebView.loadUrl("https://www.meteo.pl/um/php/meteorogram_id_um.php?ntype=0u&id=$airportId")
    }


    private class MeteoWebViewClient : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            view?.loadUrl(
                """javascript:(function() { 

	document.getElementById("wtg_meteorogram_top").style.display="none";
	document.getElementById("wtg_meteorogram_right").style.display="none";
	document.getElementById("wtg_meteorogram_bottom").style.display="none";

})()"""
            )
        }
    }

    private fun initButton(code: String) {
        val airportFullName = airportName[code] ?: "Unknown airport"
        Log.d("marker", "clicked $code")
        val intent = Intent(context, AirbaseDataFullInfo::class.java)
        intent.putExtra("airbaseFullName", airportFullName)
        intent.putExtra("airbaseName", code)
        requireContext().startActivity(intent)

    }
}