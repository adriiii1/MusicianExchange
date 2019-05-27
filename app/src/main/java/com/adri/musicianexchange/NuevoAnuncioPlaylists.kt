package com.adri.musicianexchange

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.nuevo_anuncio_playlists.*
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationResponse
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import com.spotify.sdk.android.authentication.AuthenticationRequest
import kaaes.spotify.webapi.android.SpotifyApi
import kaaes.spotify.webapi.android.SpotifyService
import kaaes.spotify.webapi.android.models.Pager
import kaaes.spotify.webapi.android.models.PlaylistSimple
import retrofit.RetrofitError

class NuevoAnuncioPlaylists : AppCompatActivity() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var tokensillo : String =""

    var listPlaylists: List<PlaylistSimple> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nuevo_anuncio_playlists)

        btSpot.setOnClickListener {
            spotLogin()
        }

        btnCargar.setOnClickListener {
            cargarListas()
        }
    }

    private fun spotLogin() {
        val requestCode = 1337
        val redirectUri = "http://musicianexchange.com/callback"
        val clientId = "00868c003ec44f00921ec16a27f18014"

        val builder = AuthenticationRequest.Builder(clientId, AuthenticationResponse.Type.TOKEN, redirectUri)

        builder.setScopes(arrayOf("streaming"))
        val request = builder.build()

        AuthenticationClient.openLoginActivity(this, requestCode, request)
    }

    private fun cargarListas(){
        val api = SpotifyApi()
        api.setAccessToken(tokensillo)
        val spotify: SpotifyService = api.service
        try {
            val thiss: Pager<PlaylistSimple> = spotify.myPlaylists
            listPlaylists = thiss.items
        }catch (error: RetrofitError){
            error.printStackTrace()
        }

        viewManager = LinearLayoutManager(this)
        viewAdapter = MiAdapterPlaylists(listPlaylists)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerNPlaylists).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    @SuppressLint("ShowToast")
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == 1337) {
            val response = AuthenticationClient.getResponse(resultCode, intent)
            when (response.type) {
                AuthenticationResponse.Type.TOKEN -> {
                    tokensillo = response.accessToken
                    Log.d("Token" , tokensillo)
                }
                AuthenticationResponse.Type.ERROR -> {
                    tokensillo = "fak"
                    Log.d("Token", tokensillo)
                }
                else -> Toast.makeText(this,"Ni idea bro, esto ha sido una guerra",Toast.LENGTH_LONG)
            }
        }
    }
}
