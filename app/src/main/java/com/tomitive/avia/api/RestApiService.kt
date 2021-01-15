package com.tomitive.avia.api

import com.tomitive.avia.model.Credentials
import com.tomitive.avia.model.LoginResponse
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
    fun loginWithToken(credentials: Credentials,
    onResult: (LoginResponse?) -> Unit){
        retrofit.loginWithToken(credentials).enqueue(
            object : Callback<LoginResponse>{
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
/*
    fun logout(cookies: String, onResult: (Response<String>?) -> Unit) {
        retrofit.logout(cookies).enqueue(
            object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("RestServiceCredentials", "FAIL: ${call.request().body()}")
                    onResult(null)
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (!response.isSuccessful) {
                        Log.d("RestServiceCredentials", "" + response.errorBody()?.string())
                    } else {
                        Log.d("RestServiceCredentials", response.headers().toString())
                    }

                    onResult(response)
                }
            }
        )
    }
    */
}
