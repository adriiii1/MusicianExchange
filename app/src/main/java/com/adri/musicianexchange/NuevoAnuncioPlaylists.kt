package com.adri.musicianexchange

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.nuevo_anuncio_playlists.*
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE
import android.content.Intent
import android.widget.Toast

class NuevoAnuncioPlaylists : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nuevo_anuncio_playlists)

        btSpot.setOnClickListener {
            spotLogin()
        }
    }

    private fun spotLogin() {
        val requestCode = 1337
        val redirectUri = "http://localhost/callback"
        val clientId = "00868c003ec44f00921ec16a27f18014"

        val builder = AuthenticationRequest.Builder(clientId, AuthenticationResponse.Type.TOKEN, redirectUri)

        builder.setScopes(arrayOf("streaming"))
        val request = builder.build()

        AuthenticationClient.openLoginActivity(this, requestCode, request)
    }

    @SuppressLint("ShowToast")
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == REQUEST_CODE) {
            val response = AuthenticationClient.getResponse(resultCode, intent)
            when (response.type) {
                AuthenticationResponse.Type.TOKEN -> {
                    Toast.makeText(this,"Ey this connected broder",Toast.LENGTH_LONG)
                }
                AuthenticationResponse.Type.ERROR -> {
                    Toast.makeText(this,"Ey this fakin broke broder",Toast.LENGTH_LONG)
                }
                else -> Toast.makeText(this,"Ni idea bro, esto ha sido una guerra",Toast.LENGTH_LONG)
            }
        }
    }
}
