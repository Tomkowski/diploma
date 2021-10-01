package com.tomitive.avia.api

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tomitive.avia.MainActivity
import com.tomitive.avia.R
import com.tomitive.avia.model.Credentials
import com.tomitive.avia.model.credentialUsername
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Aktywność logowania. Pierwsza aktywność, którą widzi użytkownik nie będąc zalogowanym
 */
class LoginActivity : AppCompatActivity() {
    /**
     * Informacja wyświetlana przy braku połączenia z serwerem
     */
    lateinit var errorToast: Toast
    /**
     * metoda wywoływana przy stworzeniu aktywności
     *
     * @param savedInstanceState mapa klucz-wartość zapisanych danych w pamięci urządzenia
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        errorToast = Toast.makeText(
            this,
            "Coś poszło nie tak. Sprawdź połączenie z internetem i spróbuj ponownie",
            Toast.LENGTH_LONG
        )
        // open url in web browser
        forgot_password.movementMethod = LinkMovementMethod.getInstance()

    }

    /**
     * metoda wysyłająca żądanie zalogowania na serwer
     *
     * @param v widok, który został wciśnięty w celu wywołania tej metody
     */
    fun onLogin(v: View) {
        progress.visibility = View.VISIBLE
        val service = RestApiService()
        val username = username.text.toString()
        val password = password.text.toString()
        service.sendCredentials(Credentials(username, password)) { response ->
            if (response == null) {
                errorToast.show()
                progress.visibility = View.INVISIBLE
                return@sendCredentials
            }
            when (response.statusCode) {
                200 -> {
                    Toast.makeText(
                        this,
                        "${response.username} has been logged in successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    val sharedPref = getSharedPreferences(getString(R.string.preferencesName),Context.MODE_PRIVATE)
                    with (sharedPref.edit()) {
                        putString(getString(R.string.sharedUsername), response.username)
                        putString(getString(R.string.sharedPassword), response.authenticatorToken)
                        apply()
                    }
                    credentialUsername = response.username
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else -> {
                    Toast.makeText(this, "Login failed, check credentials", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            progress.visibility = View.INVISIBLE
        }
    }
}