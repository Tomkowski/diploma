package com.tomitive.avia.api
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * obiekt tworzący instancję Retrofit
 */
object ServiceBuilder {
    /**
     * Klient wysyłający żądania
     */
    private val client = OkHttpClient.Builder().build()

    /**
     * instancja retrofit generowana za pomocą wzorca projektowego Builder
     */
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.0.73:8080/") //10.0.2.2 for AVD
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    /**
     * metoda zwracająca gotowy do używania obiekt Retrofit
     *
     * @param T interfejs serwisu
     * @param service serwis API
     */
    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
}