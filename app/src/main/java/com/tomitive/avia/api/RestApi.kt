package com.tomitive.avia.api

import com.tomitive.avia.model.*
import retrofit2.Call
import retrofit2.http.*

interface RestApi {

    @POST("authorize")
    fun sendCredentials(@Body credentials: Credentials) : Call<LoginResponse>

    @POST("authorize/token")
    fun loginWithToken(@Body credentials: Credentials) : Call<LoginResponse>

    @POST("authorize/logout")
    fun logout(@Body credentials: Credentials): Call<String>

    @POST("/reservation/add")
    fun createReservation(@Body reservation: ReservationRequest): Call<String>

    @POST("/reservation/all")
    fun fetchAllReservations(@Body credentials: Credentials): Call<List<Reservation>?>

    @PUT("/reservation/cancel")
    fun cancelReservation(@Body cancellationRequest: CancellationRequest): Call<String>
}