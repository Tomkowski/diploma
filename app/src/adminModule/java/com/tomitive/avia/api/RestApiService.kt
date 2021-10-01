package com.tomitive.avia.api

import com.tomitive.avia.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * obiekt Retrofit implementujący metody opisane w [RestApi]
 */
val retrofit = ServiceBuilder.buildService(RestApi::class.java)

/**
 * Serwis udostępniający użytkownikowi metody używane do komunikacji z serwerem
 */
class RestApiService {
    /**
     * metoda wysyłająca żądanie logowania (login i hasło) na serwer
     *
     * @param credentials dane użytkownika
     * @param onResult funkcja wywoływana po odebraniu odpowiedzi z serwera
     */
    fun sendCredentials(
        credentials: Credentials,
        onResult: (LoginResponse?) -> Unit
    ) {
        retrofit.sendCredentials(credentials).enqueue(
            object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    onResult(null)
                }

                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    onResult(response.body())
                }
            }
        )
    }
    /**
     * metoda wysyłająca żądanie logowania (login i token) na serwer
     *
     * @param credentials dane użytkownika
     * @param onResult funkcja wywoływana po odebraniu odpowiedzi z serwera
     */
    fun loginWithToken(
        credentials: Credentials,
        onResult: (LoginResponse?) -> Unit
    ) {
        retrofit.loginWithToken(credentials).enqueue(
            object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    onResult(null)
                }

                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    onResult(response.body())
                }

            }
        )
    }
    /**
     * metoda wysyłająca żądanie wylogowania na serwer
     *
     * @param credentials dane użytkownika
     * @param onResult funkcja wywoływana po odebraniu odpowiedzi z serwera
     */
    fun logout(credentials: Credentials, onResult: (Response<String>?) -> Unit) {
        retrofit.logout(credentials).enqueue(
            object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    onResult(null)
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    onResult(response)
                }
            }
        )
    }
    /**
     * metoda wysyłająca żądanie dodania rezerwacji
     *
     * @param reservationRequest szczegóły rezerwacji
     * @param onResult funkcja wywoływana po odebraniu odpowiedzi z serwera
     */
    fun createReservation(reservationRequest: ReservationRequest, onResult: (String?) -> Unit) {
        retrofit.createReservation(reservationRequest).enqueue(
            object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    onResult(null)
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    onResult(response.body())
                }

            }
        )
    }
    /**
     * metoda pobierająca z serwera wszystkie aktywne rezerwacje
     *
     * @param credentials dane użytkownika
     * @param onResult funkcja wywoływana po odebraniu odpowiedzi z serwera
     */
    fun fetchAllReservations(credentials: Credentials, onResult: (List<Reservation>?) -> Unit) {

        val result = retrofit.fetchAllReservations(credentials).execute()
        if (result.isSuccessful) {
            onResult(result.body())
        } else onResult(null)
    }
    /**
     * metoda wysyłająca żądanie anulowania rezerwacji
     *
     * @param cancellationRequest szczegóły rezerwacji
     * @param onResult funkcja wywoływana po odebraniu odpowiedzi z serwera
     */
    fun cancelReservation(cancellationRequest: CancellationRequest, onResult: (String?) -> Unit) {
        retrofit.cancelReservation(cancellationRequest).enqueue(
            object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    onResult(null)
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    onResult(response.body())
                }
            }
        )
    }

}