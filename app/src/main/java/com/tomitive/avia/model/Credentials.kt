package com.tomitive.avia.model

import com.google.gson.annotations.SerializedName

/**
 * Klasa przechowująca informacje użytkownika
 *
 * @property username login użytkownika
 * @property password token użytkownika
 */
data class Credentials (
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)

lateinit var credentialUsername: String