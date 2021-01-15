package com.tomitive.avia.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("username") val username: String,
    @SerializedName("authenticatorToken") val authenticatorToken: String
)