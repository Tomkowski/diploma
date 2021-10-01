package com.tomitive.avia.model

import com.google.gson.annotations.SerializedName

/**
 * Klasa przechowująca odpowiedź z serwera na wysłane dane logowania
 *
 * @property statusCode kod przesłany przez serwer po zalogowaniu
 * @property username login użytkownika
 * @property authenticatorToken wygenerowany token użytkownika
 */
data class LoginResponse(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("username") val username: String,
    @SerializedName("authenticatorToken") val authenticatorToken: String
)



