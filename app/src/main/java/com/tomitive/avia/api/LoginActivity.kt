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
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    lateinit var errorToast: Toast

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
        loginAutomatically()
    }

    private fun loginAutomatically(){
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        val username = sharedPref.getString(getString(R.string.sharedUsername), null)
        val password = sharedPref.getString(getString(R.string.sharedPassword), null)
        if(username == null || password == null) return

        val service = RestApiService()
        service.loginWithToken(Credentials(username, password)){ response ->
            if(response == null) return@loginWithToken

            when (response.statusCode) {
                200 -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

    }

    fun onLogin(v: View) {
        progress.visibility = View.VISIBLE
        val service = RestApiService()
        val username = username.text.toString()
        val password = password.text.toString()
        service.sendCredentials(Credentials(username, password)) { response ->
            if (response == null) {
                errorToast.show()
                return@sendCredentials
            }

            when (response.statusCode) {
                200 -> {
                    Toast.makeText(
                        this,
                        "${response.username} has been logged in successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    val sharedPref = getPreferences(Context.MODE_PRIVATE)
                    with (sharedPref.edit()) {
                        putString(getString(R.string.sharedUsername), response.username)
                        putString(getString(R.string.sharedPassword), response.authenticatorToken)
                        apply()
                    }
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

    fun onLogout(v: View) {
        /*
        val service = RestApiService()
        service.logout(cookies) { response ->
            if (response == null) {
                errorToast.show()
                return@logout
            }
            val successfulAuthorizationPage =
                Jspoon.create().adapter(SuccessfulAuthorizationPage::class.java).fromHtml(
                    response.body()
                )
            if (successfulAuthorizationPage.loginMessage == "Wylogowałeś się z CAS - Centralnej Usługi Uwierzytelniania.") {
                Toast.makeText(this, "User was logged off successfully", Toast.LENGTH_SHORT).show()
                cookies = ""
            } else {
                Log.d("MainActivity", "${response.body()}")
                errorToast.show()
            }


        }
        */

    }
}