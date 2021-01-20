package com.tomitive.avia.api

import android.util.Log
import com.tomitive.avia.model.Credentials
import com.tomitive.avia.model.LoginResponse
import com.tomitive.avia.model.Reservation
import com.tomitive.avia.model.ReservationRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

val retrofit = ServiceBuilder.buildService(RestApi::class.java)

class RestApiService {
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

    fun fetchAllReservations(credentials: Credentials, onResult: (List<Reservation>?) -> Unit) {
        retrofit.fetchAllReservations(credentials).enqueue(
            object : Callback<List<Reservation>?> {
                override fun onFailure(call: Call<List<Reservation>?>, t: Throwable) {
                    onResult(null)
                }

                override fun onResponse(
                    call: Call<List<Reservation>?>,
                    response: Response<List<Reservation>?>
                ) {
                    onResult(response.body())
                }
            }
        )
    }
}