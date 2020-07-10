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
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tomitive.avia.AirbaseDataFullInfo
import com.tomitive.avia.R
import com.tomitive.avia.utils.NetworkManager
import com.tomitive.avia.utils.airportMeteoLinks
import com.tomitive.avia.utils.airportName


class MarkerFragment : Fragment() {
    private lateinit var code: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.marker_fragment, container, false)
        rootView.setOnClickListener {
            Toast.makeText(context, "Web clicked", Toast.LENGTH_SHORT).show()
        }
        if(arguments == null){
            Toast.makeText(context, "arguments are empty", Toast.LENGTH_SHORT).show()
        }
        code = arguments?.getString("title") ?: ""

        if(code.isNullOrEmpty()) { Log.d("Marker", "code is empty");return rootView}
        initWebWiev(rootView)
        initButton(rootView)


        return rootView
    }

    private fun initWebWiev(rootView: View) {
        val meteoWebView = rootView.findViewById<WebView>(R.id.meteo_web_view)
        with(meteoWebView.settings) {
            setAppCachePath("${requireContext().cacheDir.absolutePath}/aviacast")
            allowFileAccess = true
            setAppCacheEnabled(true)
            javaScriptEnabled = true
            cacheMode = WebSettings.LOAD_DEFAULT
            if (NetworkManager.isNetworkAvailable(requireContext())) {
                cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            }
        }
        var airportId = airportMeteoLinks[code]

        Toast.makeText(requireContext(), "Fragment openened with code :$airportId", Toast.LENGTH_SHORT).show()

        if (airportId == null) {
            Toast.makeText(requireContext(), "Unable to find that airport!", Toast.LENGTH_SHORT)
                .show()
            airportId = "1391"
        }
        meteoWebView.webViewClient = WebViewClient()
        meteoWebView.visibility = View.VISIBLE
        meteoWebView.loadUrl("https://www.meteo.pl/um/php/meteorogram_id_um.php?ntype=0u&id=$airportId")
    }

    private fun initButton(rootView: View) {
        val airportFullName = airportName[code] ?: "Unknown airport"
        val meteoButton = rootView.findViewById<Button>(R.id.meteo_button)
        Log.d("marker", "clicked")
        meteoButton.setOnClickListener {
            val intent = Intent(context, AirbaseDataFullInfo::class.java)
            intent.putExtra("airbaseFullName", airportFullName)
            intent.putExtra("airbaseName", code)
            requireContext().startActivity(intent)
        }
    }
}