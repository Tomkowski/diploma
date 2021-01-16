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
}