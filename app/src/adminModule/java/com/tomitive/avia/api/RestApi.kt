package com.tomitive.avia.api

import com.tomitive.avia.model.*
import retrofit2.Call
import retrofit2.http.*

/**
 * interfejs przechowujący wszystkie możliwe żądania do wysłania na serwer z poziomu aplikacji
 */
interface RestApi {

    /**
     * metoda wysyłająca żądanie logowania (login i hasło) na serwer
     *
     * @param credentials dane użytkownika
     * @return
     */
    @POST("authorize/admin")
    fun sendCredentials(@Body credentials: Credentials) : Call<LoginResponse>

    /**
     * metoda wysyłająca żądania zalogowania (login i token) na serwer
     *
     * @param credentials dane użytkownika
     * @return
     */
    @POST("authorize/token")
    fun loginWithToken(@Body credentials: Credentials) : Call<LoginResponse>

    /**
     * metoda wysyłająca żądanie wylogowania (login i token) na serwer
     *
     * @param credentials dane użytkownika
     * @return
     */
    @POST("authorize/logout")
    fun logout(@Body credentials: Credentials): Call<String>

    /**
     * metoda wysyłająca żądanie dodania rezerwacji
     *
     * @param reservation szczegóły rezerwacji
     * @return
     */
    @POST("/reservation/add")
    fun createReservation(@Body reservation: ReservationRequest): Call<String>

    /**
     * metoda pobierająca z serwera wszystkie aktywne rezerwacje
     *
     * @param credentials dane użytkownika
     * @return
     */
    @POST("/reservation/all")
    fun fetchAllReservations(@Body credentials: Credentials): Call<List<Reservation>?>
    /**
     * metoda wysyłająca żądanie anulowania rezerwacji
     *
     * @param reservation szczegóły rezerwacji
     * @return
     */
    @PUT("/reservation/cancel")
    fun cancelReservation(@Body cancellationRequest: CancellationRequest): Call<String>
}